package com.tokopedia.abstraction.base.view.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import static android.app.Activity.RESULT_OK;
@Deprecated
public class CommonWebViewClient extends WebChromeClient {

    public static final int PROGRESS_COMPLETED = 100;
    public final static int ATTACH_FILE_REQUEST = 1;
    private ValueCallback<Uri> callbackBeforeL;
    public ValueCallback<Uri[]> callbackAfterL;
    ProgressBar progressBar;
    FilePickerInterface filePickerInterface;
    Context context;

    public CommonWebViewClient(FilePickerInterface filePickerInterface, ProgressBar progressBar) {
        if (filePickerInterface instanceof Activity || filePickerInterface instanceof Fragment) {
            this.filePickerInterface = filePickerInterface;
            this.progressBar = progressBar;
        } else {
            throw new RuntimeException("Should be instance of Activity or Fragmant");
        }

    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == PROGRESS_COMPLETED && progressBar != null) {
            view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        super.onProgressChanged(view, newProgress);
    }

    //For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        callbackBeforeL = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        filePickerInterface.startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
    public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
        callbackBeforeL = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        filePickerInterface.startActivityForResult(
                Intent.createChooser(i, "File Browser"), ATTACH_FILE_REQUEST);
    }

    //For Android 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        callbackBeforeL = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        filePickerInterface.startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    //For Android 5.0+
    public boolean onShowFileChooser(
            WebView webView, ValueCallback<Uri[]> filePathCallback,
            WebChromeClient.FileChooserParams fileChooserParams) {
        if (callbackAfterL != null) {
            callbackAfterL.onReceiveValue(null);
        }
        callbackAfterL = filePathCallback;

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent[] intentArray = new Intent[0];

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
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

                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
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
}
