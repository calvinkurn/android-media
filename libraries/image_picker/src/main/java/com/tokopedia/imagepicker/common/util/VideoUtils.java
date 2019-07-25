package com.tokopedia.imagepicker.common.util;

import java.io.File;

public class VideoUtils {
    private static final String TEMP_FILE     = "tkpd_video.temp";
    private static final String VIDEO_EXT     = ".mp4";
    private static final String DIR_PREFIX    = "tkpdvideo";
    private static final String RESULT_DIR    = String.format("%s%s/", FileUtils.TOKOPEDIA_DIRECTORY, DIR_PREFIX);

    public static File getVideoPath(){
        File publicDir = FileUtils.getTokopediaPublicDirectory(RESULT_DIR);
        return new File(publicDir.getAbsolutePath(), FileUtils.generateUniqueFileName()+VIDEO_EXT);
    }

    public static void deleteCacheDir(){
        File resultDir = FileUtils.getTokopediaPublicDirectory(RESULT_DIR);
        if (resultDir.exists()) {
            File[] files = resultDir.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
            resultDir.delete();
        }
    }

    public static File generateTempVideoFile(String path){
        File dir = FileUtils.getTokopediaPublicDirectory(path);
        return new File(dir.getAbsolutePath(), TEMP_FILE);
    }

    public static File moveToDir(File sourceFile, String path){
        File file = null;

        try {
            file = FileUtils.getTokopediaPublicDirectory(path);
            if (sourceFile != null){
                FileUtils.copyFile(sourceFile.getAbsolutePath(), file.getAbsolutePath());
            }
        } catch (Throwable t){ }

        if (file == null) return null;

        if (file.exists()) return file;
        else return null;
    }
}
