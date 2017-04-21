package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by m.normansyah on 12/8/15.
 */
public class FileUtils {

    public static final String CACHE_TOKOPEDIA = "/cache/tokopedia/";

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

    public static String generateUniqueFileName (String path){
        return String.valueOf(path.hashCode() ).replaceAll("-", "");
    }

    /**
     * will wrte the buffer to Tkpdpath with the filename supply. Extension will be .jpg
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/cache/tokopedia/IMG_451274244.jpg
     * @param buffer result of compressed image in jpeg
     * @param fileName name of file to write to Tkpd Path
     * @return
     */
    public static File writeImageToTkpdPath(byte[] buffer, String fileName) {
        if (buffer != null) {
            File photo = getTkpdCacheFile(fileName);
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

    public static File writeImageToTkpdPath(InputStream source, String fileName) {
        File photo = getTkpdCacheFile(fileName);

        if (photo.exists()) {
            photo.delete();
        }
        if (writeStreamToFile(source, photo)) {
            return photo;
        }

        return null;
    }

    public static File getTkpdCacheFile(String fileName){
        String externalDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String tkpdFolderPath = FileUtils.getFolderPathForUploadNoRand(externalDirPath);

        File tkpdRootdirectory = new File(tkpdFolderPath);
        if (!tkpdRootdirectory.exists()) {
            tkpdRootdirectory.mkdirs();
        }
        File photo = new File(tkpdRootdirectory.getAbsolutePath() + CACHE_TOKOPEDIA+fileName +".jpg");
        if (photo.exists()) {
            return photo;
        }
        else {
            return null;
        }
    }

    // URI starts with "content://gmail-ls/"
    public static String getPathFromGmail(Context context, Uri contentUri) {
        File attach;
        try {
            InputStream attachment = context.getContentResolver().openInputStream(contentUri);
            String fileName = FileUtils.generateUniqueFileName(contentUri.toString());
            attach = FileUtils.writeImageToTkpdPath(attachment, fileName);
            if (attach == null) {
                return null;
            }
            return attach.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return getPath(inContext, Uri.parse(path));
    }

    public static String getPath(Context context, Uri contentUri) {

        String res = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } else {
            return contentUri.getPath();
        }
        return res;
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
    private static boolean writeStreamToFile(InputStream source, File file) {
        OutputStream outStream;
        try {
            outStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = source.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            source.close();
            outStream.close();
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