package com.tokopedia.core.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;

import java.io.File;
import java.util.UUID;

/**
 * Created by Tkpd_Eka on 6/30/2015.
 */
public class ImageUploadHandler {

    public static final int CODE_UPLOAD_IMAGE = 789;
    public static final String FILELOC = "fileloc";

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

    private void startActivity(Intent intent, int code) {
        if (activity != null)
            activity.startActivityForResult(intent, code);
        else
            fragment.startActivityForResult(intent, code);
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
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

}
