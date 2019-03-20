package com.tokopedia.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.RouteManager;

import static android.app.Activity.RESULT_OK;


public abstract class BaseWebViewFragment extends BaseDaggerFragment {
    private static final int MAX_PROGRESS = 100;

    protected TkpdWebView webView;
    private ProgressBar progressBar;
    private ValueCallback<Uri> uploadMessageBeforeLolipop;
    public ValueCallback<Uri[]> uploadMessageAfterLolipop;
    public final static int ATTACH_FILE_REQUEST = 1;

    /**
     * return the url to load in the webview
     * You can use URLGenerator.java to use generate the seamless URL.
     */
    protected abstract String getUrl();

    /**
     * this is to put in header with key X-User-ID when the webview loadUrl
     * fill with blank or NULL if authorization header is not needed.
     */
    @Nullable
    protected abstract String getUserIdForHeader();

    /**
     * this is to put in header
     */
    @Nullable
    protected abstract String getAccessToken();

    @Override
    protected void initInjector() {

    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == MAX_PROGRESS) {
                onLoadFinished();
            }
            super.onProgressChanged(view, newProgress);
        }
        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserBeforeLolipop(uploadMsg);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            if (uploadMessageAfterLolipop != null) {
                uploadMessageAfterLolipop.onReceiveValue(null);
            }
            uploadMessageAfterLolipop = filePathCallback;

            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray = new Intent[0];

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, ATTACH_FILE_REQUEST);
            return true;

        }
    }

    private void openFileChooserBeforeLolipop(ValueCallback<Uri> uploadMessage){
        uploadMessageBeforeLolipop = uploadMessage;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        webView = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        return view;
    }

    protected int getLayout(){
        return R.layout.fragment_general_web_view_lib;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setIndeterminate(true);

        if(!TextUtils.isEmpty(getUrl())) {
            loadWeb();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == uploadMessageAfterLolipop) {
                        return;
                    }

                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};

                    }
                }
            }
            uploadMessageAfterLolipop.onReceiveValue(results);
            uploadMessageAfterLolipop = null;
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == uploadMessageBeforeLolipop) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                uploadMessageBeforeLolipop.onReceiveValue(result);
                uploadMessageBeforeLolipop = null;
            }
        }
    }

    protected void loadWeb() {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebViewClient());
        webView.loadAuthUrl(getUrl(), getUserIdForHeader(), getAccessToken());
        webView.setWebViewClient(getWebviewClient());
    }

    @NonNull
    protected WebViewClient getWebviewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return BaseWebViewFragment.this.shouldOverrideUrlLoading(view, url);
            }
        };
    }

    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (url.contains("tokopedia://")) {
            RouteManager.route(getActivity(), url);
        }
        return false;
    }

    protected void onLoadFinished(){
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
