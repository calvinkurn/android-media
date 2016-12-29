package com.tokopedia.core.util;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;

/**
 * Created by nisie on 10/7/16.
 */
public abstract class TkpdWebViewClient extends WebViewClient {
    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        super.onReceivedSslError(view, handler, error);
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.notification_error_ssl_cert_invalid);
        builder.setPositiveButton(R.string.title_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(R.string.title_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        final Uri uri = Uri.parse(url);
        return onOverrideUrl(uri);
    }

    //    @TargetApi(Build.VERSION_CODES.N)
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        return onOverrideUrl(request.getUrl());
//    }

    public static Uri generateUri(Uri uri) {
        String url = String.valueOf(uri);
        url = "?flag_app=1&&device=android&app_version=" + GlobalConfig.VERSION_CODE;
        return Uri.parse(url);
    }

    public static String generateUri(String uri) {
        String url = String.valueOf(uri);
        url = "?flag_app=1&&device=android&app_version=" + GlobalConfig.VERSION_CODE;
        return url;
    }

    protected abstract boolean onOverrideUrl(Uri url);
}