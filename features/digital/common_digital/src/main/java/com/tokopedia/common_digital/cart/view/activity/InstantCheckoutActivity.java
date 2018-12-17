package com.tokopedia.common_digital.cart.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.common_digital.R;
import com.tokopedia.common_digital.cart.view.model.checkout.InstantCheckoutData;
import com.tokopedia.common_digital.common.DigitalRouter;
import com.tokopedia.common_digital.common.di.DaggerDigitalComponent;
import com.tokopedia.common_digital.common.di.DigitalComponent;
import com.tokopedia.network.constant.ErrorNetMessage;

import javax.inject.Inject;

/**
 * Created by Rizky on 30/08/18.
 */
public class InstantCheckoutActivity extends BaseSimpleActivity {

    public static final int REQUEST_CODE = 2001;
    private static final long FORCE_TIMEOUT = 60000L;
    private static final String TAG = InstantCheckoutActivity.class.getSimpleName();

    private static final String SEAMLESS = "seamless";

    public static final String EXTRA_INSTANT_CHECKOUT_DATA = "EXTRA_INSTANT_CHECKOUT_DATA";

    private InstantCheckoutData instantCheckoutData;

    @Inject
    DigitalRouter digitalRouter;

    TkpdWebView webView;
    ProgressBar progressBar;

    public static Intent newInstance(Context context, InstantCheckoutData instantCheckoutData) {
        return new Intent(context, InstantCheckoutActivity.class)
                .putExtra(EXTRA_INSTANT_CHECKOUT_DATA, instantCheckoutData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instantCheckoutData = getIntent().getParcelableExtra(EXTRA_INSTANT_CHECKOUT_DATA);

        super.onCreate(savedInstanceState);

        initInjector();
    }

    private void initInjector() {
        DigitalComponent digitalComponent = DaggerDigitalComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build();
        digitalComponent.inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_digital_instant_checkout;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        super.setupLayout(savedInstanceState);

        webView = findViewById(R.id.webview);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new InstantCheckoutWebViewClient());
        webView.setWebChromeClient(new InstantCheckoutWebViewChromeClient());
        webView.setOnKeyListener(getWebViewOnKeyListener());

        webView.loadUrl(instantCheckoutData.getRedirectUrl());
    }

    private View.OnKeyListener getWebViewOnKeyListener() {
        return (v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        finish();
                        return true;
                }
            }
            return false;
        };
    }


    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    private class InstantCheckoutWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @SuppressWarnings("deprecation")
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {

        }

        public boolean onConsoleMessage(ConsoleMessage cm) {

            return true;
        }
    }

    private class InstantCheckoutWebViewClient extends WebViewClient {

        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ((!instantCheckoutData.getFailedCallbackUrl().isEmpty()
                    && url.contains(instantCheckoutData.getFailedCallbackUrl()))
                    || (!instantCheckoutData.getSuccessCallbackUrl().isEmpty()
                    && url.contains(instantCheckoutData.getSuccessCallbackUrl()))) {
                view.stopLoading();
                finish();
                return true;
            } else {
                if (digitalRouter != null
                        && digitalRouter.isSupportApplink(url)
                        && digitalRouter.getIntentDeepLinkHandlerActivity() != null) {
                    Intent intent = digitalRouter.getIntentDeepLinkHandlerActivity();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finish();
                    return true;
                } else if (digitalRouter != null) {
                    String urlFinal = digitalRouter.getGeneratedOverrideRedirectUrlPayment(url);
                    if (urlFinal == null)
                        return super.shouldOverrideUrlLoading(view, url);
                    view.loadUrl(
                            urlFinal,
                            digitalRouter.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal)
                    );
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
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
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            //    Log.d(TAG, "start url instant instantCheckout = " + url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(FORCE_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (timeout) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showError(view, WebViewClient.ERROR_TIMEOUT);
                            }
                        });
                    }
                }
            }).start();
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
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

    private void showToastMessageWithForceCloseView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

}
