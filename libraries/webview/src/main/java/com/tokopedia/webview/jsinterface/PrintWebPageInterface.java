package com.tokopedia.webview.jsinterface;

import android.app.Activity;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

public final class PrintWebPageInterface {

    private static final String JOB_NAME = "Current Page";

    private WeakReference<Activity> mContextRef;
    private WeakReference<WebView> mWebViewRef;

    public PrintWebPageInterface(Activity context, WebView webView) {
        this.mContextRef = new WeakReference<Activity>(context);
        this.mWebViewRef = new WeakReference<WebView>(webView);
    }

    @JavascriptInterface
    public void printCurrentScreen() {
        if (mContextRef.get() == null || mWebViewRef.get() == null) {
            return;
        }
        try {
            PrintManager printManager = ContextCompat.getSystemService(mContextRef.get(), PrintManager.class);
            PrintDocumentAdapter adapter;
            PrintAttributes.Builder printAttrBuilder = new PrintAttributes.Builder();
            PrintAttributes printAttr = printAttrBuilder.setMediaSize(
                    PrintAttributes.MediaSize.ISO_A4
            ).build();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                adapter = mWebViewRef.get().createPrintDocumentAdapter(JOB_NAME);
            } else {
                adapter = mWebViewRef.get().createPrintDocumentAdapter();
            }

            if (printManager != null) {
                printManager.print(JOB_NAME, adapter, printAttr);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}