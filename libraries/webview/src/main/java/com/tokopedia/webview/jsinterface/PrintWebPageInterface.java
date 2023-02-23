package com.tokopedia.webview.jsinterface;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;

import kotlin.jvm.Volatile;

public final class PrintWebPageInterface {

    private static final String DEFAULT_JOB_NAME_PREFIX = "download-";
    private static int DELAY = 500;

    private WeakReference<Activity> mContextRef;
    private WeakReference<WebView> mWebViewRef;

    private Handler mHandler;
    private Runnable mRunnable;


    @Volatile
    private long lastTimestampCalled;

    public PrintWebPageInterface(Activity context, WebView webView) {
        this.mContextRef = new WeakReference<Activity>(context);
        this.mWebViewRef = new WeakReference<WebView>(webView);
    }

    @JavascriptInterface
    public void printCurrentScreen(final String filename) {
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                executePrint(filename);
            }
        };
        mHandler.post(mRunnable);
    }

    private void executePrint(String filename) {
        try {
            if (mContextRef.get() == null || mWebViewRef.get() == null) {
                return;
            }
            long currentTimestampCalled = System.currentTimeMillis();
            if (lastTimestampCalled + DELAY >= currentTimestampCalled) {
                return;
            }
            lastTimestampCalled = System.currentTimeMillis();
            PrintManager printManager = ContextCompat.getSystemService(mContextRef.get(), PrintManager.class);
            PrintDocumentAdapter adapter;
            PrintAttributes.Builder printAttrBuilder = new PrintAttributes.Builder();
            PrintAttributes printAttr = printAttrBuilder.setMediaSize(
                    PrintAttributes.MediaSize.ISO_A4
            ).build();

            String jobName = "";
            if (filename.isEmpty()) {
                jobName = DEFAULT_JOB_NAME_PREFIX + currentTimestampCalled;
            } else {
                jobName = filename;
            }

            adapter = mWebViewRef.get().createPrintDocumentAdapter(jobName);

            if (printManager != null) {
                printManager.print(jobName, adapter, printAttr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}