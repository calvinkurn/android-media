package com.tokopedia.kelontongapp.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by meta on 12/10/18.
 */
public class KelontongWebviewClient extends WebViewClient {

    private static final int PERMISSION_REQUEST_CODE = 2312;

    private Activity activity;

    public KelontongWebviewClient(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        final Uri uri = Uri.parse(url);
        return handleUri(view, uri);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri uri = request.getUrl();
        return handleUri(view, uri);
    }

    private boolean handleUri(WebView view, final Uri uri) {
        view.loadUrl(uri.toString());
        return true;
    }

    public boolean checkPermission() {
        int resultCamera = ContextCompat.checkSelfPermission(activity, CAMERA);
        int resultWrite = ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultWrite == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (!(writeAccepted && cameraAccepted))
                        Toast.makeText(activity, "Mohon untuk memberikan izin akses kamera dan menulis file.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (activity.shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("Mohon untuk memberikan izin akses kamera dan menulis file.",
                                        (dialog, which) -> requestPermission());
                                return;
                            }
                        }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Batal", null)
                .create()
                .show();
    }
}
