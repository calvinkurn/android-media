package com.tokopedia.core.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.myproduct.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Tkpd_Eka on 6/30/2015.
 */
public class ImageUploadHandler {

    public static final int CODE_UPLOAD_IMAGE = 789;
    public static final String FILELOC = "fileloc";
    private static final String TAG = ImageUploadHandler.class.getSimpleName();

    public static ImageUploadHandler createInstance(Activity activity) {
        ImageUploadHandler uploadimage = new ImageUploadHandler();
        uploadimage.activity = activity;
        uploadimage.context = activity;
        return uploadimage;
    }

    public static ImageUploadHandler createInstance(Fragment fragment) {
        ImageUploadHandler uploadimage = new ImageUploadHandler();
        uploadimage.fragment = fragment;
        uploadimage.context = fragment.getActivity();
        return uploadimage;
    }

    public class Model {
        public String cameraFileLoc;
    }

    public static final int REQUEST_CODE = 111;
    public static final int REQUEST_CODE_GALLERY = 1243;

    private Activity activity;
    private Fragment fragment;
    private Context context;
    private Model model = new Model();

    public void actionImagePicker() {
        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        startActivity(imageGallery, ImageGallery.TOKOPEDIA_GALLERY);
    }

    public void actionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CODE);
    }

    public String actionCamera2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CODE);
        return model.cameraFileLoc;
    }

    public Intent getCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        return intent;
    }

    private void startActivity(Intent intent, int code) {
        if (activity != null)
            activity.startActivityForResult(intent, code);
        else
            fragment.startActivityForResult(intent, code);
    }

    public Uri getOutputMediaFileUri() {
        return MethodChecker.getUri(context, getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        String imageCode = uniqueCode();
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory() + File.separator
                        + "Tokopedia" + File.separator);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + imageCode + ".jpg");
        model.cameraFileLoc = Environment.getExternalStorageDirectory() + File.separator
                + "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";
        return mediaFile;
    }

    private String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        String code = id.substring(0, 16);
        return code;
    }

    public String getCameraFileloc() {
        return model.cameraFileLoc;
    }

    public void setImageBitmap(String fileloc) {
        this.model.cameraFileLoc = fileloc;
    }

    public static File writeImageToTkpdPath(byte[] buffer) throws IOException {
        File directory = new File(FileUtils.getFolderPathForUploadRandom());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File photo = new File(directory.getAbsolutePath() + "/image.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        FileOutputStream fos = new FileOutputStream(photo.getPath());

        fos.write(buffer);
        fos.close();

        return photo;
    }

    public static byte[] compressImage(String path) throws IOException {
        Log.d(TAG, "lokasi yang mau diupload " + path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;

        tempPic = ImageHandler.RotatedBitmap(tempPic, path);

        if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
            tempPicToUpload = ImageHandler.ResizeBitmap(tempPic, 2048);
        }
// else if(tempPic.getWidth() < 300 || tempPic.getHeight() < 100){
//            tempPicToUpload = ih.ResizeBitmap(tempPic, 300);
//        }
        else {
            tempPicToUpload = tempPic;
        }
        tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
        return bao.toByteArray();
    }

}
