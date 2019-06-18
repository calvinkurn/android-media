package com.tokopedia.imagepicker.common.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.media.ExifInterface;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;

import static com.tokopedia.imagepicker.common.util.ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE;
import static com.tokopedia.imagepicker.common.util.ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA;
import static com.tokopedia.imagepicker.common.util.ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_EDIT_RESULT;

/**
 * Created by hendry on 24/04/18.
 */

public class ImageUtils {
    private static final int IMAGE_WIDTH_MIN_HD = 1280;

    public static final int DEF_WIDTH = 2560;
    public static final int DEF_HEIGHT = 2560;

    private static final String TEMP_FILE_NAME = "temp.tmp";
    public static final String PNG_EXT = ".png";
    public static final String JPG_EXT = ".jpg";
    public static final String PNG = "png";
    public static final String TOKOPEDIA_FOLDER_PREFIX = "Tokopedia";
    public static final String TOKOPEDIA_DIRECTORY = "Tokopedia/";

    @StringDef({DIRECTORY_TOKOPEDIA_CACHE, DIRECTORY_TOKOPEDIA_CACHE_CAMERA, DIRECTORY_TOKOPEDIA_EDIT_RESULT})
    public @interface DirectoryDef {
        String DIRECTORY_TOKOPEDIA_CACHE = TOKOPEDIA_DIRECTORY + TOKOPEDIA_FOLDER_PREFIX + " Cache/";
        String DIRECTORY_TOKOPEDIA_CACHE_CAMERA = TOKOPEDIA_DIRECTORY + TOKOPEDIA_FOLDER_PREFIX + " Camera/";
        String DIRECTORY_TOKOPEDIA_EDIT_RESULT = TOKOPEDIA_DIRECTORY + TOKOPEDIA_FOLDER_PREFIX + " Edit/";
    }

    public static File getTokopediaPublicDirectory(@DirectoryDef String directoryType) {
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

    public static File getTokopediaPhotoPath(@DirectoryDef String directoryDef, boolean isPng) {
        File directory = getTokopediaPublicDirectory(directoryDef);
        return new File(directory.getAbsolutePath(), generateUniqueFileName() + (isPng ? PNG_EXT : JPG_EXT));
    }

    public static File getTokopediaPhotoPath(@DirectoryDef String directoryDef, String referencePath) {
        return getTokopediaPhotoPath(directoryDef, isPng(referencePath));
    }

    public static void deleteCacheFolder(@DirectoryDef String directoryString) {
        File directory = getTokopediaPublicDirectory(directoryString);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    files[i].delete();
                }
            }
            directory.delete();
        }
    }

    public static void deleteFileInTokopediaFolder(String filesToDelete) {
        if (TextUtils.isEmpty(filesToDelete)) {
            return;
        }
        File fileToDelete = new File(filesToDelete);
        if (isInTokopediaDirectory(fileToDelete)) {
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

    /**
     * check if the file is in Tokopedia directory.
     */
    private static boolean isInTokopediaDirectory(File file) {
        return file.exists() && file.getAbsolutePath().contains(TOKOPEDIA_DIRECTORY);
    }

    private static boolean isInTokopediaDirectory(String filePath, @DirectoryDef String directory) {
        return filePath.contains(directory);
    }

    public static boolean isPng(String referencePath) {
        return referencePath.endsWith(PNG_EXT);
    }

    /**
     * write byte buffer to Cache File int TkpdCacheDirectory
     * This "cache file" is a representation of the bytes.
     */
    public static File writeImageToTkpdPath(@DirectoryDef String directoryDef, byte[] buffer, boolean isPng) {
        if (buffer != null) {
            File photo = getTokopediaPhotoPath(directoryDef, isPng);
            if (photo.exists()) {
                photo.delete();
            }
            if (writeBufferToFile(buffer, photo.getPath())) {
                return photo;
            }
        }
        return null;
    }

    /**
     * copy the bitmap (might from gallery or camera path) to Tkpd Cache Directory
     */
    public static File writeImageToTkpdPath(@DirectoryDef String directoryDef, String galleryOrCameraPath) {
        File file;
        try {
            file = getTokopediaPhotoPath(directoryDef, galleryOrCameraPath);
            copyFile(galleryOrCameraPath, file.getAbsolutePath());
        } catch (Throwable e) {
            return null;
        }
        if (file.exists()) {
            return file;
        } else {
            return null;
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

    public static ArrayList<String> copyFiles(ArrayList<String> cropppedImagePaths,
                                              @DirectoryDef String directoryDef) throws IOException {
        ArrayList<String> resultList = new ArrayList<>();
        for (String imagePathFrom : cropppedImagePaths) {
            String resultPath = copyFileToDirectory(imagePathFrom, directoryDef);
            resultList.add(resultPath);
        }
        return resultList;
    }

    public static String copyFileToDirectory(String imagePathFrom,
                                             @DirectoryDef String directoryDef) throws IOException {
        if (isInTokopediaDirectory(imagePathFrom, directoryDef)) {
            return imagePathFrom;
        } else {
            File outputFile = getTokopediaPhotoPath(directoryDef, imagePathFrom);
            String resultPath = outputFile.getAbsolutePath();
            copyFile(imagePathFrom, resultPath);
            return resultPath;
        }
    }

    public static void deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * compress the bitmap, then write to Tkpd Cache Directory
     */
    public static File writeImageToTkpdPath(@DirectoryDef String directoryDef, Bitmap bitmap, boolean isPng) {
        File file = getTokopediaPhotoPath(directoryDef, isPng);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(isPng ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * copy the inputstream to Tkpd Cache Directory
     * The file represents the copy of the original bitmap and can be deleted/modified
     * without changing the original image
     */
    public static File writeImageToTkpdPath(@DirectoryDef String directoryDef, InputStream source, boolean isPng) {
        File photo = getTokopediaPhotoPath(directoryDef, isPng);
        if (photo.exists()) {
            photo.delete();
        }
        if (writeStreamToFile(source, photo)) {
            return photo;
        }
        return null;
    }

    // URI starts with "content://gmail-ls/"
    public static String getPathFromGmail(Context context, Uri contentUri, @DirectoryDef String directoryDef) {
        File attach;
        try {
            InputStream attachment = context.getContentResolver().openInputStream(contentUri);
            attach = ImageUtils.writeImageToTkpdPath(directoryDef, attachment, isPNGMimeType(getMimeType(context, contentUri)));
            if (attach == null) {
                return null;
            }
            return attach.getAbsolutePath();
        } catch (Throwable e) {
            return null;
        }
    }

    public static String getTkpdPathFromURI(Context context, Uri uri, @DirectoryDef String directoryDef) {
        InputStream is = null;
        String mimeType = getMimeType(context, uri);
        if (!isImageMimeType(mimeType)) {
            return null;
        }
        boolean isPNG = isPNGMimeType(mimeType);
        if (uri.getAuthority() != null) {
            try {
                Bitmap bmp = null;
                int inSampleSize = 1;
                boolean oomError;
                do {
                    try {
                        is = context.getContentResolver().openInputStream(uri);
                        if (is == null) {
                            return null;
                        }
                        // estimate sample size
                        if (inSampleSize == 1 && is.available() >
                                (1.5 * IMAGE_WIDTH_MIN_HD * IMAGE_WIDTH_MIN_HD)) {
                            inSampleSize = 2;
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = inSampleSize;
                        bmp = BitmapFactory.decodeStream(is, null, options);
                        if (bmp == null) {
                            return null;
                        }
                        oomError = false;
                    } catch (OutOfMemoryError outOfMemoryError) {
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
                File file = writeImageToTkpdPath(directoryDef, bmp, isPNG);
                bmp.recycle();
                if (file != null) {
                    return file.getAbsolutePath();
                } else {
                    return null;
                }
            } catch (Throwable e) {
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

    public static int[] getWidthAndHeight(String filePath) {
        return getWidthAndHeight(new File(filePath));
    }

    public static int[] getWidthAndHeight(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return new int[]{options.outWidth, options.outHeight};
    }

    public static long getFileSizeInKb(String filePath) {
        return getFileSizeInKb(new File(filePath));
    }

    public static long getFileSizeInKb(File file) {
        return file.length() / 1024;
    }

    public static int getMinResolution(String filePath) {
        return getMinResolution(new File(filePath));
    }

    public static int getMinResolution(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        return Math.min(options.outWidth, options.outHeight);
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

    private static boolean isImageMimeType(String mimeType) {
        return !TextUtils.isEmpty(mimeType) && mimeType.startsWith("image");
    }

    public static boolean isImageType(Context context, String filePath) {
        return isImageMimeType(context, Uri.fromFile(new File(filePath)));
    }

    private static boolean isPNGMimeType(String mimeType) {
        return !TextUtils.isEmpty(mimeType) && mimeType.contains(PNG);
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

    public static String trimBitmap(String imagePath, float expectedRatio, float currentRatio, boolean needCheckRotate,
                                    @DirectoryDef String targetDirectory) {
        Bitmap bitmapToEdit = ImageUtils.getBitmapFromPath(imagePath, ImageUtils.DEF_WIDTH,
                ImageUtils.DEF_HEIGHT, needCheckRotate);
        int width = bitmapToEdit.getWidth();
        int height = bitmapToEdit.getHeight();
        int left = 0, right = width, top = 0, bottom = height;
        int expectedWidth = width, expectedHeight = height;
        if (expectedRatio < currentRatio) { // trim left and right
            expectedWidth = (int) (expectedRatio * height);
            left = ((width - expectedWidth) / 2);
            right = (left + expectedWidth);
        } else { // trim top and bottom
            expectedHeight = (int) (width / expectedRatio);
            top = ((height - expectedHeight) / 2);
            bottom = (top + expectedHeight);
        }

        boolean isPng = ImageUtils.isPng(imagePath);

        Bitmap outputBitmap = null;
        try {
            outputBitmap = Bitmap.createBitmap(expectedWidth, expectedHeight, bitmapToEdit.getConfig());
            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawBitmap(bitmapToEdit, new Rect(left, top, right, bottom),
                    new Rect(0, 0, expectedWidth, expectedHeight), null);
            File file = ImageUtils.writeImageToTkpdPath(targetDirectory, outputBitmap, isPng);
            bitmapToEdit.recycle();
            outputBitmap.recycle();
            System.gc();

            return file.getAbsolutePath();
        }catch (Throwable e) {
            if (outputBitmap!=null &&!outputBitmap.isRecycled()) {
                outputBitmap.recycle();
            }
            return imagePath;
        }
    }

    public static String resizeBitmap(String imagePath, int maxWidth, int maxHeight, boolean needCheckRotate,
                                      @DirectoryDef String resultDirectory) {
        Bitmap bitmapToEdit = ImageUtils.getBitmapFromPath(imagePath, maxWidth, maxHeight, needCheckRotate);

        boolean isPng = ImageUtils.isPng(imagePath);

        Bitmap outputBitmap;
        try {
            outputBitmap = Bitmap.createBitmap(bitmapToEdit.getWidth(), bitmapToEdit.getHeight(), bitmapToEdit.getConfig());
            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawBitmap(bitmapToEdit, 0, 0, null);
            File file = ImageUtils.writeImageToTkpdPath(resultDirectory,
                    outputBitmap, isPng);
            bitmapToEdit.recycle();
            outputBitmap.recycle();

            System.gc();
            return file.getAbsolutePath();
        } catch (Throwable e) {
            return imagePath;
        }
    }

    public static File resizeBitmapToFile(String imagePath, int maxWidth, int maxHeight, boolean needCheckRotate,
                                      @DirectoryDef String resultDirectory) {
        Bitmap bitmapToEdit = ImageUtils.getBitmapFromPath(imagePath, maxWidth, maxHeight, needCheckRotate);

        boolean isPng = ImageUtils.isPng(imagePath);

        Bitmap outputBitmap;
        try {
            outputBitmap = Bitmap.createBitmap(bitmapToEdit.getWidth(), bitmapToEdit.getHeight(), bitmapToEdit.getConfig());
            Canvas canvas = new Canvas(outputBitmap);
            canvas.drawBitmap(bitmapToEdit, 0, 0, null);
            File file = ImageUtils.writeImageToTkpdPath(resultDirectory,
                    outputBitmap, isPng);
            bitmapToEdit.recycle();
            outputBitmap.recycle();

            System.gc();
            return file;
        } catch (Throwable e) {
            return null;
        }
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
            return false;
        }
    }


    public static Bitmap getBitmapFromPath(String imagePath, int maxWidth, int maxHeight, boolean needCheckRotate) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        Bitmap tempPic = null;

        boolean decodeAttemptSuccess = false;
        while (!decodeAttemptSuccess) {
            try {
                tempPic = BitmapFactory.decodeFile(imagePath, options);
                decodeAttemptSuccess = true;
            } catch (OutOfMemoryError error) {
                options.inSampleSize *= 2;
            }
        }

        if (needCheckRotate) {
            if (tempPic != null) {
                try {
                    return rotate(tempPic, imagePath);
                } catch (Throwable e1) {
                    return tempPic;
                }
            }
        }
        return tempPic;
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width lower or equal to the requested height and width.
            while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotate(Bitmap bitmap, Context context, Uri uri) throws IOException {
        int orientation = ExifInterface.ORIENTATION_NORMAL;
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                return bitmap;
            }
            ExifInterface exif = new ExifInterface(inputStream);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation == ExifInterface.ORIENTATION_NORMAL) {
                return bitmap;
            }
        } catch (Throwable e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return rotateBitmap(bitmap, orientation);
    }

    public static Bitmap rotate(Bitmap bitmap, String path) throws IOException {
        int orientation = getOrientation(path);
        if (orientation == ExifInterface.ORIENTATION_NORMAL) {
            return bitmap;
        }
        return rotateBitmap(bitmap, orientation);
    }

    public static int getOrientation(String path) throws IOException {
        try {
            ExifInterface exif = new ExifInterface(path);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (Throwable e) {
            return ExifInterface.ORIENTATION_NORMAL;
        }
    }

    public static int getOrientation(ExifInterface exif) {
        return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError ignore) {
            return null;
        }
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.setRotate(degree);

        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError ignore) {
            return null;
        }
    }

    public static Bitmap resize(Bitmap bitmap, float bounding) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float xScale = bounding / width;
        float yScale = bounding / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        bitmap.recycle();
        return resultBitmap;
    }

    public static Bitmap brightBitmap(Bitmap bitmap, float brightness) {
        float[] colorTransform = {
                1, 0, 0, 0, brightness,
                0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness,
                0, 0, 0, 1, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        try {
            Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), true);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(resultBitmap, 0, 0, paint);

            bitmap.recycle();

            return resultBitmap;
        } catch (OutOfMemoryError error) {
            return bitmap;
        }
    }

    public static Bitmap contrastBitmap(Bitmap bitmap, float contrast) {
        float[] colorTransform = new float[]{
                contrast, 0, 0, 0, 0,
                0, contrast, 0, 0, 0,
                0, 0, contrast, 0, 0,
                0, 0, 0, 1, 0};

        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0f);
        colorMatrix.set(colorTransform);

        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);

        try {
            Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), true);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(resultBitmap, 0, 0, paint);

            bitmap.recycle();
            return resultBitmap;
        } catch (OutOfMemoryError outOfMemoryError) {
            return bitmap;
        }

    }

    public static Bitmap convertToMutable(Bitmap imgIn) throws Exception {
        //this is the file going to use temporally to save the bytes.
        // This file will not be a image, it will store the raw image data.
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + TEMP_FILE_NAME);

        //Open an RandomAccessFile
        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        //into AndroidManifest.xml file
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        // get the width and height of the source bitmap.
        int width = imgIn.getWidth();
        int height = imgIn.getHeight();
        Bitmap.Config type = imgIn.getConfig();

        //Copy the byte to the file
        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
        imgIn.copyPixelsToBuffer(map);
        //recycle the source bitmap, this will be no longer used.
        imgIn.recycle();
        System.gc();// try to force the bytes from the imgIn to be released

        //Create a new bitmap to load the bitmap again. Probably the memory will be available.
        imgIn = Bitmap.createBitmap(width, height, type);
        map.position(0);
        //load it back from temporary
        imgIn.copyPixelsFromBuffer(map);
        //close the temporary file and channel , then delete that also
        channel.close();
        randomAccessFile.close();

        // delete the temp file
        file.delete();
        return imgIn;
    }
}
