package com.tokopedia.core.manage.people.profile.customdialog;

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
import com.tokopedia.core.util.MethodChecker;

import java.io.File;
import java.util.UUID;

/**
 * Created on 2/29/16.
 */
public class UploadImageDialog {

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_GALLERY = 2;
    private final Context context;
    private Activity activity;
    private Fragment fragment;
    private String cameraFileLoc;

    public interface UploadImageDialogListener {
        void onSuccess(String data);

        void onFailed();
    }

    public UploadImageDialog(Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
    }

    public UploadImageDialog(Activity activity) {
        this.activity = activity;
        this.context = activity;
    }

    public void openImagePicker() {
        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        startActivity(imageGallery, REQUEST_GALLERY);
    }

    public void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CAMERA);
    }

    private Uri getOutputMediaFileUri() {
        return MethodChecker.getUri(context, getOutputMediaFile());
    }

    private void startActivity(Intent intent, int requestCode) {
        if (fragment != null)
            fragment.startActivityForResult(intent, requestCode);

        else if (activity != null)
            activity.startActivityForResult(intent, requestCode);
    }

    public File getOutputMediaFile() {
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

        cameraFileLoc = Environment.getExternalStorageDirectory() + File.separator
                + "Tokopedia" + File.separator + "IMG_" + imageCode + ".jpg";

        return mediaFile;
    }

    private String uniqueCode() {
        String IDunique = UUID.randomUUID().toString();
        String id = IDunique.replaceAll("-", "");
        return id.substring(0, 16);
    }

    public void onResult(int requestCode, int resultCode, Intent intent, UploadImageDialogListener listener) {
        if (requestCode == REQUEST_CAMERA || requestCode == REQUEST_GALLERY) {
            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    BitmapFactory.Options checksize = new BitmapFactory.Options();
                    checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    checksize.inJustDecodeBounds = true;

                    BitmapFactory
                            .decodeFile(intent.getStringExtra(ImageGallery.EXTRA_URL), checksize);
                    options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
                    Bitmap tempPic = BitmapFactory
                            .decodeFile(intent.getStringExtra(ImageGallery.EXTRA_URL), options);
                    if (tempPic != null) {
                        listener.onSuccess(intent.getStringExtra(ImageGallery.EXTRA_URL));
                    } else {
                        listener.onFailed();
                    }
                    break;
                case Activity.RESULT_OK:
                    if (!(cameraFileLoc == null || cameraFileLoc.isEmpty())) {
                        listener.onSuccess(cameraFileLoc);
                    } else {
                        listener.onFailed();
                    }
                    break;
            }
        }
    }

}
