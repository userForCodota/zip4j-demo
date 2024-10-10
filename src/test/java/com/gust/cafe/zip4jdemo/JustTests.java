package com.gust.cafe.zip4jdemo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import org.junit.jupiter.api.Test;

import java.io.File;

public class JustTests {
    @Test
    @SneakyThrows
    public void testCreateSplitZip() {
        String mp4 = "C:\\Users\\Administrator\\Desktop\\test\\001.mp4";
        String splitZipPath = "C:\\Users\\Administrator\\Desktop\\test\\单文件拆分式压缩\\001.zip";// 拆分式压缩之后的中心文件
        // ZipFile 不会帮我们处理mkdir
        FileUtil.mkdir(FileUtil.getParent(splitZipPath, 1));
        // 创建 ZipFile 对象
        ZipFile zipFile = new ZipFile(splitZipPath);

        // 开启监控
        zipFile.setRunInThread(true);
        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();

        // 创建拆分式压缩文件
        zipFile.createSplitZipFile(
                ListUtil.toList(new File(mp4)),// 需要压缩的文件, 可以是多个
                new ZipParameters(),// 压缩参数,这里不关注密码等场景
                true, // 是否必须分割存档
                1024 * 1024 * 10 // 如果归档必须分割，则必须分割的字节长度, 这里是 10MB
        );

        while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
            // 百分比完成度
            System.out.println("Percentage done: " + progressMonitor.getPercentDone());
            System.out.println("Current file: " + progressMonitor.getFileName());
            System.out.println("Current task: " + progressMonitor.getCurrentTask());

            Thread.sleep(100);
        }

        if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
            System.out.println("Successfully added folder to zip");
        } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
            System.out.println("Error occurred. Error message: " + progressMonitor.getException().getMessage());
        } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
            System.out.println("Task cancelled");
        }
    }
}
