package com.tokopedia.core.myproduct.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.app.MainApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by m.normansyah on 12/8/15.
 */
public class FileUtils {
    public static final String CACHE_TOKOPEDIA = "/cache/tokopedia/";
    public static final String FILE_IMAGE_EXT = ".png"; //to handle transparent issues.
    public static final int DEF_WIDTH_CMPR = 2048;
    public static final int DEF_QLTY_COMPRESS = 100;

    /**
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/1451274244/
     */
    public static String getFolderPathForUploadRandom() {
        return getFolderPathForUpload() + (System.currentTimeMillis() / 1000L) + new Random().nextInt(1000) + "/";
    }

    public static String getFolderPathForUpload() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + MainApplication.PACKAGE_NAME + "/";
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
     * compress the bitmap, then write to Tkpd Cache Directory
     * The file represents the copy of the original bitmap and can be deleted/modified
     * without changing the original image
     */
    public static File writeImageToTkpdPath(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            byte[] bytes;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
            bytes = bao.toByteArray();
            return writeImageToTkpdPath(bytes);
        } else {
            return null;
        }
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

    /**
     * copy the inputstream to Tkpd Cache Directory
     * The file represents the copy of the original bitmap and can be deleted/modified
     * without changing the original image
     */
    public static File writeImageToTkpdPath(InputStream source) {
        String fileName = generateUniqueFileName();
        File photo = getTkpdImageCacheFile(fileName);

        if (photo.exists()) {
            photo.delete();
        }
        if (writeStreamToFile(source, photo)) {
            return photo;
        }
        return null;
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

    /**
     * delete the inputted files (only process files in tkpd cache directory)
     * If the files are not in tkpd cache directory, ignore those.
     */
    public static void deleteAllCacheTkpdFiles(ArrayList<String> filesToDelete) {
        if (filesToDelete == null || filesToDelete.size() == 0) {
            return;
        }
        for (int i = 0, sizei = filesToDelete.size(); i < sizei; i++) {
            String filePathToDelete = filesToDelete.get(i);
            deleteAllCacheTkpdFile(filePathToDelete);
        }
    }

    /**
     * delete the inputted file (only process files in tkpd cache directory)
     * If the file is not in tkpd cache directory, ignore it.
     */
    public static void deleteAllCacheTkpdFile(String fileToDeletePath) {
        if (TextUtils.isEmpty(fileToDeletePath)) {
            return;
        }
        File fileToDelete = new File(fileToDeletePath);
        if (isInTkpdCache(fileToDelete)) {
            fileToDelete.delete();
        }
    }

    // URI starts with "content://gmail-ls/"
    public static String getPathFromGmail(Context context, Uri contentUri) {
        File attach;
        try {
            InputStream attachment = context.getContentResolver().openInputStream(contentUri);
            attach = FileUtils.writeImageToTkpdPath(attachment);
            if (attach == null) {
                return null;
            }
            return attach.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTkpdPathFromURI(Context context, Uri uri) {
        InputStream is = null;
        if (!isImageMimeType(context, uri)) {
            return null;
        }
        if (uri.getAuthority() != null) {
            try {
                String path = getPathFromMediaUri(context, uri);
                if (TextUtils.isEmpty(path)) {
                    path = getPath(context, uri);
                }
                if (TextUtils.isEmpty(path)) {
                    return null;
                }
                Bitmap bmp = null;
                int inSampleSize = 1;
                boolean oomError;
                do {
                    try {
                        is = context.getContentResolver().openInputStream(uri);
                        if (is == null) {
                            return null;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = inSampleSize;
                        bmp = BitmapFactory.decodeStream(is, null, options);
                        if (bmp == null) {
                            return null;
                        }
                        bmp = ImageHandler.RotatedBitmap(bmp, path);
                        oomError = false;
                    } catch (OutOfMemoryError outOfMemoryError) {
                        if (bmp != null) {
                            bmp.recycle();
                            bmp = null;
                        }
                        inSampleSize *= 2;
                        oomError = true;
                        if (inSampleSize > 16) {
                            break;
                        }
                    }
                } while (oomError);
                if (bmp == null) {
                    return null;
                }
                File file = writeImageToTkpdPath(bmp);
                bmp.recycle();
                if (file != null) {
                    return file.getAbsolutePath();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private static boolean isImageMimeType(Context context, Uri uri) {
        String mimeType = getMimeType(context, uri);
        return !TextUtils.isEmpty(mimeType) && mimeType.startsWith("image");
    }

    public static String getPathFromMediaUri(Context context, Uri contentUri) {

        String res = "";
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    res = cursor.getString(column_index);
                    return res;
                }
            } catch (Exception e) {
                return null;
            } finally {
                cursor.close();
            }
        } else {
            return contentUri.getPath();
        }
        return res;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && isDocumentURI(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = getDocumentID(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = getDocumentID(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = getDocumentID(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @TargetApi(19)
    private static boolean isDocumentURI(Context context, Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }

    @TargetApi(19)
    private static String getDocumentID(Uri uri) {
        return DocumentsContract.getDocumentId(uri);
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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