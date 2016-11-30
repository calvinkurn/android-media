package com.tokopedia.core.payment.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
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

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.payment.model.responsedynamicpayment.DynamicPaymentData;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;

/**
 * @author by Angga.Prasetiyo on 19/05/2016.
 */
public class DynamicPaymentFragment extends BasePresenterFragment
        implements View.OnKeyListener {
    private static final String TAG = DynamicPaymentFragment.class.getSimpleName();
    private static final String EXTRA_ARGS_DYNAMIC_PAYMENT_DATA = "EXTRA_ARGS_DYNAMIC_PAYMENT_DATA";
    private static final String MESSAGE_PAYMENT_SUCCESS = "Pembayaran Berhasil";
    private static final String MESSAGE_PAYMENT_CANCELED = "Proses pembayaran dibatalkan";
    private static final String MESSAGE_PAYMENT_FAILED = "Proses pembayaran gagal, coba kembali";
    private static final String KEY_QUERY_PAYMENT_ID = "id";
    private static final String KEY_QUERY_LD = "ld";
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTAINS_ACCOUNT_URL = "accounts.tokopedia.com";
    private static final String CONTAINS_LOGIN_URL = "login.pl";
    private static final long FORCE_TIMEOUT = 60000L;

    @BindView(R2.id.webview)
    WebView webView;
    @BindView(R2.id.progressbar)
    ProgressBar progressBar;

    private DynamicPaymentData dynamicPaymentData;
    private ActionListener actionListener;
    private String paymentId;

    public static DynamicPaymentFragment newInstance(DynamicPaymentData data) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ARGS_DYNAMIC_PAYMENT_DATA, data);
        DynamicPaymentFragment fragment = new DynamicPaymentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return true;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {
        actionListener = (ActionListener) activity;
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        this.dynamicPaymentData = arguments.getParcelable(EXTRA_ARGS_DYNAMIC_PAYMENT_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_fragment_general_web_view;
    }

    @Override
    protected void initView(View view) {

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void setViewListener() {
        progressBar.setIndeterminate(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new DynamicPaymentWebViewClient());
        webView.setWebChromeClient(new DynamicPaymentWebViewChromeClient());
        webView.setOnKeyListener(this);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        webView.postUrl(dynamicPaymentData.getRedirectUrl(),
                EncodingUtils.getBytes(dynamicPaymentData.getQueryString(), CHARSET_UTF_8));
    }

    public String getPaymentId() {
        return paymentId;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (paymentId != null && !paymentId.isEmpty()) {
                        actionListener.onDynamicPaymentSuccess(paymentId, MESSAGE_PAYMENT_SUCCESS);
                    } else {
                        actionListener.onDynamicPaymentCanceled(MESSAGE_PAYMENT_CANCELED);
                    }
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (paymentId != null && !paymentId.isEmpty()) {
                    actionListener.onDynamicPaymentSuccess(paymentId, MESSAGE_PAYMENT_SUCCESS);
                } else {
                    actionListener.onDynamicPaymentCanceled(MESSAGE_PAYMENT_CANCELED);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface ActionListener {
        void onDynamicPaymentSuccess(String paymentId, String message);

        void onDynamicPaymentFailed(String message);

        void onDynamicPaymentCanceled(String message);
    }

    private class DynamicPaymentWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.invalidate();
            paymentId = Uri.parse(url).getQueryParameter(KEY_QUERY_PAYMENT_ID);
            if (url.contains(dynamicPaymentData.getCallbackUrlPath())) {
                Uri uri = Uri.parse(url);
                String id = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                paymentId = id;
                actionListener.onDynamicPaymentSuccess(id, MESSAGE_PAYMENT_SUCCESS);
                return true;
            } else if (url.contains(CONTAINS_ACCOUNT_URL)) {
                Uri uriMain = Uri.parse(url);
                String ld = uriMain.getQueryParameter(KEY_QUERY_LD);
                String urlThanks;
                try {
                    urlThanks = URLDecoder.decode(ld, CHARSET_UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    urlThanks = "";
                }
                Uri uri = Uri.parse(urlThanks);
                String id = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
                view.stopLoading();
                paymentId = id;
                actionListener.onDynamicPaymentSuccess(id, MESSAGE_PAYMENT_SUCCESS);
                return true;
            } else if (url.contains(CONTAINS_LOGIN_URL)) {
                view.stopLoading();
                actionListener.onDynamicPaymentFailed(MESSAGE_PAYMENT_FAILED);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, url);
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
            showError(view, error.getErrorCode());
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(FORCE_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (timeout) {
                        getActivity().runOnUiThread(new Runnable() {
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
            String message = "";
            switch (errorCode) {
                case WebViewClient.ERROR_TIMEOUT:
                    message = ErrorNetMessage.MESSAGE_ERROR_TIMEOUT;
                    break;
                default:
                    message = ErrorNetMessage.MESSAGE_ERROR_DEFAULT;
                    break;
            }
            view.stopLoading();
            if (actionListener != null) actionListener.onDynamicPaymentFailed(message);
        }
    }

    private class DynamicPaymentWebViewChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
        }

        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d(TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
            return true;
        }
    }
}
