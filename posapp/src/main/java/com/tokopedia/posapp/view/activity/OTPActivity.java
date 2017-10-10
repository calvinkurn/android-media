package com.tokopedia.posapp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.deeplink.Constants;
import com.tokopedia.posapp.view.OTP;
import com.tokopedia.posapp.view.presenter.OTPPresenter;
import com.tokopedia.posapp.view.viewmodel.otp.OTPData;

import java.util.List;

/**
 * Created by okasurya on 10/4/17.
 */

public class OTPActivity extends BasePresenterActivity<OTP.Presenter>
        implements HasComponent, OTP.View {
    public static final long FORCE_TIMEOUT = 90000L;

    private WebView scroogeWebView;
    private ProgressBar progressBar;

    @DeepLink(Constants.Applinks.OTP)
    public static Intent newInstance(Context context, Bundle extras) {
        Uri uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build();

        Intent intent = new Intent(context, OTPActivity.class);
        intent.setData(uri);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        presenter = new OTPPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_otp;
    }

    @Override
    protected void initView() {
        scroogeWebView = (WebView) findViewById(com.tokopedia.payment.R.id.scrooge_webview);
        progressBar = (ProgressBar) findViewById(com.tokopedia.payment.R.id.progressbar);

    }

    @Override
    protected void setViewListener() {
        progressBar.setIndeterminate(true);
        scroogeWebView.getSettings().setJavaScriptEnabled(true);
        scroogeWebView.getSettings().setDomStorageEnabled(true);
        scroogeWebView.getSettings().setBuiltInZoomControls(false);
        scroogeWebView.getSettings().setDisplayZoomControls(true);
        scroogeWebView.getSettings().setAppCacheEnabled(true);
        scroogeWebView.setWebViewClient(new OTPWebViewClient());
        scroogeWebView.setWebChromeClient(new OTPWebViewChromeClient());
        scroogeWebView.setOnKeyListener(getWebViewOnKeyListener());
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {
        presenter.initializeData(getIntent().getStringExtra("extras"));
    }

    @Override
    public void getOTPWebview(OTPData data) {
        scroogeWebView.loadUrl(data.getUrl());
    }

    @Override
    public void postOTPWebview(OTPData data) {
        scroogeWebView.postUrl(data.getUrl(), data.getParameters());
    }

    @Override
    public void onLoadDataError(Throwable e) {
        e.printStackTrace();
        CommonUtils.UniversalToast(this, e.getMessage());
        goToCart();
    }

    private void goToCart() {
        finish();
        startActivity(LocalCartActivity.newTopInstance(this));
    }

    @Override
    public void onLoadDataError(List<String> errorList) {
        if(errorList.get(0) != null) CommonUtils.UniversalToast(this, errorList.get(0));
        goToCart();
    }

    private class OTPWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            CommonUtils.UniversalToast(OTPActivity.this, url);
            Log.d("pos o2o otp url", url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            if (progressBar != null) progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request,
                                    WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }


        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            Log.d("pos o2o", "initial url = " + url);
            progressBar.setVisibility(View.VISIBLE);
            if(url.contains("/payment/thanks")) {
                presenter.checkPaymentState(url);
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        private void showError(WebView view, int errorCode) {
            String message;
            switch (errorCode) {
                case WebViewClient.ERROR_TIMEOUT:
                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                    break;
                default:
                    message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                    break;
            }
            view.stopLoading();
            showToastMessageWithForceCloseView(message);
        }
    }

    private class OTPWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                hideProgressBar();
            }
            super.onProgressChanged(view, newProgress);
        }

        @SuppressWarnings("deprecation")
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            //        Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            //        Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }

    private void hideProgressBar() {
        hideProgressLoading();
    }

    private void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private View.OnKeyListener getWebViewOnKeyListener() {
        return null;
    }

    public void showToastMessageWithForceCloseView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}
