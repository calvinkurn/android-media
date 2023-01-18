package com.tokopedia.webview.jsinterface;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

import kotlin.jvm.Volatile;
import kotlinx.coroutines.Job;

public final class PrintWebPageInterface {

    private static final String JOB_NAME = "Current Page";

    private WeakReference<Activity> mContextRef;
    private WeakReference<WebView> mWebViewRef;

    private Handler mHandler;
    private Runnable mRunnable;

    private int delay = 500;

    @Volatile
    private long lastTimestampCalled;

    public PrintWebPageInterface(Activity context, WebView webView) {
        this.mContextRef = new WeakReference<Activity>(context);
        this.mWebViewRef = new WeakReference<WebView>(webView);
    }

    private void cancelJob() {
        if (mHandler == null || mRunnable == null) {
            return;
        }
        mHandler.removeCallbacks(mRunnable);
    }

    @JavascriptInterface
    public void printCurrentScreen() {
        cancelJob();
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mContextRef.get() == null || mWebViewRef.get() == null) {
                        return;
                    }
                    long currentTimestampCalled = System.currentTimeMillis();
                    if (lastTimestampCalled + delay >= currentTimestampCalled) {
                        return;
                    }
                    lastTimestampCalled = System.currentTimeMillis();
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
                } catch (Exception e) {
                    Log.d("MyApplication", "Throw: " + e.toString());
                    e.printStackTrace();
                }
            }
        };
        mHandler.post(mRunnable);
    }
}