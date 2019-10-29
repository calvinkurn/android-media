package com.tokopedia.tkpdreactnative.react.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import androidx.annotation.NonNull;

import com.tokopedia.config.GlobalConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileUtils {
    public static final String CACHE_TOKOPEDIA = "/cache/tokopedia/";
    public static final String FILE_IMAGE_EXT = ".png"; //to handle transparent issues.
    public static final int DEF_WIDTH_CMPR = 2048;
    public static final int DEF_QLTY_COMPRESS = 100;

    public static String getFolderPathForUpload() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + GlobalConfig.getPackageApplicationName() + "/";
    }

    public static String generateUniqueFileName() {
        return String.valueOf(System.currentTimeMillis() / 1000L) + new Random().nextInt(1000);
    }

    @NonNull
    private static File getTkpdCacheDirectory() {
        String tkpdFolderPath = FileUtils.getFolderPathForUpload();

        File tkpdRootdirectory = new File(tkpdFolderPath);
        if (!tkpdRootdirectory.exists()) {
            tkpdRootdirectory.mkdirs();
        }
        File tkpdCachedirectory = new File(tkpdRootdirectory.getAbsolutePath() + CACHE_TOKOPEDIA);
        if (!tkpdCachedirectory.exists()) {
            tkpdCachedirectory.mkdirs();
        }
        return tkpdCachedirectory;
    }

    @NonNull
    public static File getTkpdImageCacheFile(String fileName) {
        File tkpdCachedirectory = getTkpdCacheDirectory();
        return new File(tkpdCachedirectory.getAbsolutePath() + "/" + fileName + FILE_IMAGE_EXT);
    }

    /**
     * write byte buffer to Cache File int TkpdCacheDirectory
     * This "cache file" is a representation of the bytes.
     */
    public static File writeImageToTkpdPath(byte[] buffer) {
        if (buffer != null) {
            String fileName = FileUtils.generateUniqueFileName();
            File photo = getTkpdImageCacheFile(fileName);
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

    /**
     * copy the bitmap (might from gallery or camera path) to Tkpd Cache Directory
     * The file represents the copy of the original bitmap and can be deleted/modified
     * without changing the original image
     */
    public static File writeImageToTkpdPath(String galleryOrCameraPath) {
        return writeImageToTkpdPath(convertLocalImagePathToBytes(galleryOrCameraPath, DEF_WIDTH_CMPR,
                DEF_WIDTH_CMPR, DEF_QLTY_COMPRESS));
    }
    public static File writeImageToTkpdPath(String galleryOrCameraPath,  int compressionQuality) {
        return writeImageToTkpdPath(convertLocalImagePathToBytes(galleryOrCameraPath, DEF_WIDTH_CMPR,
                DEF_WIDTH_CMPR, compressionQuality));
    }


    /**
     * check if the file is in tkpdcache directory.
     */
    public static boolean isInTkpdCache(File file) {
        File tkpdCacheDirectory = getTkpdCacheDirectory();
        String tkpdcacheDirPath = tkpdCacheDirectory.getAbsolutePath();
        if (file.exists() && file.getAbsolutePath().contains(tkpdcacheDirPath)) {
            return true;
        }
        return false;
    }

    private static boolean writeBufferToFile(byte[] buffer, String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);

            fos.write(buffer);
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public static byte[] convertLocalImagePathToBytes(String imagePathToCompress, int maxWidth, int maxHeight, int compressionQuality) {
        Bitmap tempPicToUpload = compressImageToBitmap(imagePathToCompress, maxWidth, maxHeight, compressionQuality);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        if (tempPicToUpload != null) {
            tempPicToUpload.compress(Bitmap.CompressFormat.PNG, compressionQuality, bao);
            return bao.toByteArray();
        }
        return null;
    }


    public static Bitmap compressImageToBitmap(String imagePathToCompress, int maxWidth, int maxHeight, int compressionQuality) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePathToCompress, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(imagePathToCompress, options);
        Bitmap tempPicToUpload;
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
            return tempPicToUpload;
        }
        return null;
    }

}