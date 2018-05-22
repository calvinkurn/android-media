package com.tokopedia.digital.cart.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
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

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.digital.R;
import com.tokopedia.digital.R2;
import com.tokopedia.digital.cart.model.InstantCheckoutData;
import com.tokopedia.payment.router.IPaymentModuleRouter;

import butterknife.BindView;

/**
 * @author anggaprasetiyo on 3/23/17.
 */

public class InstantCheckoutActivity extends BasePresenterActivity {
    public static final int REQUEST_CODE = InstantCheckoutActivity.class.hashCode();
    private static final long FORCE_TIMEOUT = 60000L;
    private static final String TAG = InstantCheckoutActivity.class.getSimpleName();

    private static final String SEAMLESS = "seamless";

    public static final String EXTRA_INSTANT_CHECKOUT_DATA = "EXTRA_INSTANT_CHECKOUT_DATA";

    @BindView(R2.id.webview)
    TkpdWebView webView;
    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    private InstantCheckoutData instantCheckoutData;
    private IPaymentModuleRouter paymentModuleRouter;


    public static Intent newInstance(Context context, InstantCheckoutData instantCheckoutData) {
        return new Intent(context, InstantCheckoutActivity.class)
                .putExtra(EXTRA_INSTANT_CHECKOUT_DATA, instantCheckoutData);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        this.instantCheckoutData = extras.getParcelable(EXTRA_INSTANT_CHECKOUT_DATA);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_instant_checkout_digital_module;
    }

    @Override
    protected void initView() {
        if (getApplication() instanceof IPaymentModuleRouter) {
            paymentModuleRouter = (IPaymentModuleRouter) getApplication();
        }
    }

    @Override
    protected void setViewListener() {
        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new InstantCheckoutWebViewClient());
        webView.setWebChromeClient(new InstantCheckoutWebViewChromeClient());
        webView.setOnKeyListener(getWebViewOnKeyListener());
    }

    private View.OnKeyListener getWebViewOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            finish();
                            return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    protected void initVar() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void setActionVar() {
        webView.loadUrl(instantCheckoutData.getRedirectUrl());
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
            //     Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            //    Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }

    private class InstantCheckoutWebViewClient extends WebViewClient {

        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //   Log.d(TAG, "redirect url instant checkout = " + url);
            if ((!instantCheckoutData.getFailedCallbackUrl().isEmpty()
                    && url.contains(instantCheckoutData.getFailedCallbackUrl()))
                    || (!instantCheckoutData.getSuccessCallbackUrl().isEmpty()
                    && url.contains(instantCheckoutData.getSuccessCallbackUrl()))) {
                view.stopLoading();
                finish();
                return true;
            } else {
                if (paymentModuleRouter != null
                        && paymentModuleRouter.isSupportedDelegateDeepLink(url)
                        && paymentModuleRouter.getIntentDeepLinkHandlerActivity() != null) {
                    Intent intent = paymentModuleRouter.getIntentDeepLinkHandlerActivity();
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finish();
                    return true;
                } else if (paymentModuleRouter != null) {
                    String urlFinal = paymentModuleRouter.getGeneratedOverrideRedirectUrlPayment(url);
                    if (urlFinal == null)
                        return super.shouldOverrideUrlLoading(view, url);
                    view.loadUrl(
                            urlFinal,
                            paymentModuleRouter.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal)
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
            //    Log.d(TAG, "start url instant checkout = " + url);
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

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
