package com.tokopedia.imagepicker.common.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

public class FileUtils {
    public static final String TOKOPEDIA_DIRECTORY = "Tokopedia/";


    /**
     * get Tokopedia's public directory
     * it will be create a new directory if doesn't exist
     * @param: path
     */
    public static File getTokopediaPublicDirectory(String directoryType) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + directoryType + "/";
        File directory = new File(filePath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return Environment.getDownloadCacheDirectory();
            }
        }
        return directory;
    }

    public static String generateUniqueFileName() {
        String timeString = String.valueOf(System.currentTimeMillis());
        int length = timeString.length();
        int startIndex = length - 5;
        if (startIndex < 0) {
            startIndex = 0;
        }
        timeString = timeString.substring(startIndex);
        return timeString + new Random().nextInt(100);
    }

    /**
     * check if the file is in Tokopedia directory.
     */
    public static boolean isInTokopediaDirectory(File file) {
        return file.exists() && file.getAbsolutePath().contains(TOKOPEDIA_DIRECTORY);
    }

    public static boolean isInTokopediaDirectory(String filePath, String directory) {
        return filePath.contains(directory);
    }

    public static void deleteFileInTokopediaFolder(String filesToDelete) {
        if (TextUtils.isEmpty(filesToDelete)) {
            return;
        }
        File fileToDelete = new File(filesToDelete);
        if (FileUtils.isInTokopediaDirectory(fileToDelete)) {
            fileToDelete.delete();
        }
    }

    public static void deleteFilesInTokopediaFolder(ArrayList<String> filesToDelete) {
        if (filesToDelete == null || filesToDelete.size() == 0) {
            return;
        }
        for (int i = 0, sizei = filesToDelete.size(); i < sizei; i++) {
            String filePathToDelete = filesToDelete.get(i);
            deleteFileInTokopediaFolder(filePathToDelete);
        }
    }

    public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo) throws IOException {
        if (pathFrom.equalsIgnoreCase(pathTo)) {
            return;
        }

        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            inputChannel = new FileInputStream(new File(pathFrom)).getChannel();
            outputChannel = new FileOutputStream(new File(pathTo)).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();
        } finally {
            if (inputChannel != null) inputChannel.close();
            if (outputChannel != null) outputChannel.close();
        }
    }
}
