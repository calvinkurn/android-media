package com.tokopedia.kelontongapp.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class KelontongWebChromeClient extends WebChromeClient {

    private static final int PROGRESS_COMPLETED = 100;
    public final static int ATTACH_FILE_REQUEST = 1;

    private ValueCallback callbackBeforeL;
    private ValueCallback<Uri[]> callbackAfterL;

    private String mCM;

    private Context context;

    private FilePickerInterface filePickerInterface;

    public KelontongWebChromeClient(Context context, FilePickerInterface filePickerInterface) {
        if (filePickerInterface instanceof Activity || filePickerInterface instanceof Fragment) {
            this.context = context;
            this.filePickerInterface = filePickerInterface;
        } else {
            throw new RuntimeException("Should be instance of Activity or Fragment");
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == PROGRESS_COMPLETED) {
            view.setVisibility(View.VISIBLE);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onShowFileChooser(
            WebView webView, ValueCallback<Uri[]> filePathCallback,
            FileChooserParams fileChooserParams) {
        if (callbackAfterL != null) {
            callbackAfterL.onReceiveValue(null);
        }
        callbackAfterL = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(context.getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCM);
            }catch(IOException ex){
                ex.printStackTrace();
            }
            if(photoFile != null){
                mCM = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            }else{
                takePictureIntent = null;
            }
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");

        Intent[] intentArray;
        if(takePictureIntent != null){
            intentArray = new Intent[]{takePictureIntent};
        }else{
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        filePickerInterface.startActivityForResult(chooserIntent, ATTACH_FILE_REQUEST);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == callbackAfterL) {
                        return;
                    }

                    if(intent == null || intent.getData() == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        } else {
                            if (intent.getClipData() != null) {
                                final int numSelectedFiles = intent.getClipData().getItemCount();
                                results = new Uri[numSelectedFiles];
                                for (int i = 0; i < numSelectedFiles; i++) {
                                    results[i] = intent.getClipData().getItemAt(i).getUri();
                                }
                            }
                        }
                    }
                }
            }
            if(callbackAfterL != null) callbackAfterL.onReceiveValue(results);
            callbackAfterL = null;
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == callbackBeforeL) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                callbackBeforeL.onReceiveValue(result);
                callbackBeforeL = null;
            }
        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }
}
