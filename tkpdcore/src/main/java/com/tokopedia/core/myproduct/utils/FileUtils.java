package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by m.normansyah on 12/8/15.
 */
public class FileUtils {

    /**
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/1451274244/
     * @param root
     * @return
     */
    public static String getFolderPathForUpload(String root){
        return root+"/Android/data/"+ MainApplication.PACKAGE_NAME+"/"+(System.currentTimeMillis() / 1000L)+new Random().nextInt(1000) + "/";
    }


    public static String getFolderPathForUploadNoRand(String root) {
        return root + "/Android/data/" + MainApplication.PACKAGE_NAME + "/";
    }

    public static String getFileNameWithoutExt(String path) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }

    /**
     * will wrte the buffer to Tkpdpath with the filename supply. Extension will be .jpg
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/cache/tokopedia/IMG_451274244.jpg
     *
     * @param buffer             result of compressed image in jpeg
     * @param fileNameWithoutExt name of file without extension to write to Tkpd Path
     * @return
     */
    public static File writeImageToTkpdPath(byte[] buffer, String fileNameWithoutExt) {
        if (buffer != null) {
            String externalDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            String tkpdFolderPath = FileUtils.getFolderPathForUploadNoRand(externalDirPath);

            File tkpdRootdirectory = new File(tkpdFolderPath);
            if (!tkpdRootdirectory.exists()) {
                tkpdRootdirectory.mkdirs();
            }
            File photo = new File(tkpdRootdirectory.getAbsolutePath() + "/cache/tokopedia/" + fileNameWithoutExt + ".jpg");
            if (photo.exists()) {
                // photo already exist in cache
                if (photo.length() == buffer.length) {
                    return photo;
                } else { // the length is different, delete it and write the new one
                    photo.delete();
                    if (writeBufferToFile(buffer, photo.getPath())) {
                        return photo;
                    }
                }
            } else {
                if (writeBufferToFile(buffer, photo.getPath())) {
                    return photo;
                }
            }
        }
        return null;
    }

    private static boolean writeBufferToFile(byte[] buffer, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);

            fos.write(buffer);
            fos.close();
            return true;
        } catch (java.io.IOException e) {
            return false;
        }

    }

    public static byte[] compressImage(String imagePathToCompress, int maxWidth, int maxHeight, int compressionQuality) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePathToCompress, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(imagePathToCompress, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;
        if (tempPic != null) {
            try {
                tempPic = ImageHandler.RotatedBitmap(tempPic, imagePathToCompress);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (tempPic.getWidth() > maxWidth || tempPic.getHeight() > maxHeight) {
                tempPicToUpload = ImageHandler.ResizeBitmap(tempPic, compressionQuality);
            } else {
                tempPicToUpload = tempPic;
            }
            tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bao);
            return bao.toByteArray();
        }
        return null;
    }

    /**
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/1451274244/image.jpg
     * @param root
     * @param output
     * @param extension
     * @return
     */
    public static String getPathForUpload(String root, String output, String extension){
        return root+"/Android/data/"+ MainApplication.PACKAGE_NAME+"/"+(System.currentTimeMillis() / 1000L) + "/"+output+"."+extension;
    }

    public static void writeStringAsFileExt(Context context, final String fileContents, String fileName) {
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            FileWriter out = new FileWriter(new File(root.getAbsolutePath())+"/"+ fileName);//new File(context.getExternalFilesDir(null)
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}