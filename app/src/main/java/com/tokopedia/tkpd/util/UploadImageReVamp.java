package com.tokopedia.tkpd.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.tkpd.GalleryBrowser;
import com.tokopedia.tkpd.ImageGallery;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.facade.FacadeUploadImage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Tkpd_Eka on 6/30/2015.
 */
public class UploadImageReVamp {

    public interface UploadImageListener{
        void onSuccess(JSONObject result);
        void onStart();
        void onFailure();
    }

    public static UploadImageReVamp createInstance(Activity activity, String fileName, String webService){
        UploadImageReVamp uploadimage = new UploadImageReVamp();
        uploadimage.activity = activity;
        uploadimage.context = activity;
        uploadimage.model.fileName = fileName;
        uploadimage.model.webService = webService;
        uploadimage.facade = FacadeUploadImage.createInstance(activity, uploadimage.model);
        return uploadimage;
    }

    public static UploadImageReVamp createInstance(Fragment fragment, String fileName, String webService){
        UploadImageReVamp uploadimage = new UploadImageReVamp();
        uploadimage.fragment = fragment;
        uploadimage.context = fragment.getActivity();
        uploadimage.model.fileName = fileName;
        uploadimage.model.webService = webService;
        uploadimage.facade = FacadeUploadImage.createInstance(fragment.getActivity(), uploadimage.model);
        return uploadimage;
    }

    public class Model{
        public Bitmap uploadImage;
        public String cameraFileLoc;
        public String fileName;
        public String serverId;
        public String uploadURL;
        public String webService;
        public ArrayList<String> paramKey = new ArrayList<>();
        public ArrayList<String> paramValue = new ArrayList<>();
    }

    public static final String WS_UPLOAD_PROOF = "/ajax/ws/proof-upload-tcpdn.pl";
    public static final String WS_SHOP_EDITOR = "/ajax/ws/shop-upload-tcpdn.pl";
    public static final String WS_RESOLUTION_CENTER = "/ajax/ws/contact-upload-tcpdn.pl";
    public static final String WS_TICKET = "/ajax/ws/contact-upload-tcpdn.pl";
    public static final String WS_PROFILE = "/ajax/ws/profile-upload-tcpdn.pl";
    public static final int REQUEST_CAMERA = 111;


    private Activity activity;
    private Fragment fragment;
    private Context context;
    private UploadImageListener listener;
    private Model model = new Model();
    private FacadeUploadImage facade;

    public void actionPickImage(){
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setMessage(context.getString(R.string.dialog_upload_option));
        myAlertDialog.setPositiveButton(context.getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actionImagePicker();
            }
        });
        myAlertDialog.setNegativeButton(context.getString(R.string.title_camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                actionCamera();
            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void addParam(String key, String value){
        model.paramKey.add(key);
        model.paramValue.add(value);
    }

    public void setOnGenerateHostListener(FacadeUploadImage.OnGenerateHostListener gnListener){
        facade.setGenerateHostListener(gnListener);
    }

    public void setOnUploadListener(UploadImageListener listener){
        this.listener = listener;
    }

    public void onResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CAMERA || requestCode == ImageGallery.TOKOPEDIA_GALLERY) {
            switch (resultCode) {
                case GalleryBrowser.RESULT_CODE:
                    getBitmapToUploadImagePicker(data);
                    actionUploadImage();
                    break;
                case Activity.RESULT_OK:
                    getBitmapToUploadCamera();
                    actionUploadImage();
                    break;
                default:
                    break;
            }
        }
    }

    //=====================================================================

    public void actionImagePicker(){
        Intent imageGallery = new Intent(context, GalleryBrowser.class);
        startActivity(imageGallery, ImageGallery.TOKOPEDIA_GALLERY);
//        context.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void actionCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
        startActivity(intent, REQUEST_CAMERA);
    }

    private void startActivity(Intent intent, int code){
        if(activity != null)
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

    private void getBitmapToUploadImagePicker(Intent data){
        String fileLoc = data.getStringExtra(ImageGallery.EXTRA_URL);
        try {
            getBitmapFromFile(fileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileLocFromCamera(){
        return model.cameraFileLoc;
    }

    private void getBitmapToUploadCamera(){
        try {
            getBitmapFromFile(model.cameraFileLoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getBitmapFromFile(String fileLoc) throws IOException{
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = getInSampleSize(fileLoc);
        model.uploadImage = BitmapFactory.decodeFile(fileLoc, options);
        if(model.uploadImage != null){
            model.uploadImage = ImageHandler.RotatedBitmap(model.uploadImage, fileLoc);
        }
    }

    private int getInSampleSize(String fileLoc){
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileLoc, checksize);
        return ImageHandler.calculateInSampleSize(checksize);
    }

    private void actionUploadImage(){
        listener.onStart();
        facade.actionUploadImage(new FacadeUploadImage.OnUploadImageListenerAWS() {
            @Override
            public void onSuccess(JSONObject result) {
                onUploadImageSuccess(result);
            }

            @Override
            public void onFailure() {
                if(listener!=null)
                    listener.onFailure();
            }

            @Override
            public void onMessageError(ArrayList<String> MessageError) {
                CommonUtils.UniversalToast(context, MessageError.toString());
                listener.onFailure();
            }
        });
    }

    private void onUploadImageSuccess(final JSONObject result){
        if(listener!=null)
            listener.onSuccess(result);
    }

}
