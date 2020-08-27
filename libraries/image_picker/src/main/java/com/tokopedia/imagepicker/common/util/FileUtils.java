package com.tokopedia.imagepicker.common.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;


public class FileUtils {
    public static final String TOKOPEDIA_DIRECTORY = "Tokopedia/";


    /**
     * get Tokopedia's public directory
     * it will be create a new directory if doesn't exist
     *
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

    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf(File.separator);
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
