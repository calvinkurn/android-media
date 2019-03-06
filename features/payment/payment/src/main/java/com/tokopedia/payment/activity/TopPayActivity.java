package com.tokopedia.payment.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient;
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.payment.BuildConfig;
import com.tokopedia.payment.R;
import com.tokopedia.payment.fingerprint.di.DaggerFingerprintComponent;
import com.tokopedia.payment.fingerprint.di.FingerprintModule;
import com.tokopedia.payment.fingerprint.util.PaymentFingerprintConstant;
import com.tokopedia.payment.fingerprint.view.FingerPrintDialogPayment;
import com.tokopedia.payment.fingerprint.view.FingerprintDialogRegister;
import com.tokopedia.payment.model.PaymentPassData;
import com.tokopedia.payment.presenter.TopPayContract;
import com.tokopedia.payment.presenter.TopPayPresenter;
import com.tokopedia.payment.router.IPaymentModuleRouter;
import com.tokopedia.payment.utils.Constant;
import com.tokopedia.payment.utils.ErrorNetMessage;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.inject.Inject;


/**
 * Created by kris on 3/9/17. Tokopedia
 */

public class TopPayActivity extends AppCompatActivity implements TopPayContract.View, FingerPrintDialogPayment.ListenerPayment, FingerprintDialogRegister.ListenerRegister, FilePickerInterface {
    private static final String TAG = TopPayActivity.class.getSimpleName();

    public static final String EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA";

    private static final String ACCOUNTS_URL = "accounts.tokopedia.com";
    public static final String KEY_QUERY_PAYMENT_ID = "id";
    public static final String KEY_QUERY_LD = "ld";
    public static final String CHARSET_UTF_8 = "UTF-8";
    private static final String LOGIN_URL = "login.pl";
    private static final String HCI_CAMERA_KTP = "android-js-call://ktp";
    private static final String HCI_CAMERA_SELFIE = "android-js-call://selfie";
    private static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    private static final String[] THANK_PAGE_URL_LIST = new String[]{"thanks", "thank"};

    public static final int PAYMENT_SUCCESS = 5;
    public static final int PAYMENT_CANCELLED = 6;
    public static final int PAYMENT_FAILED = 7;
    public static final int HCI_CAMERA_REQUEST_CODE = 978;
    public static final long FORCE_TIMEOUT = 90000L;

    @Inject
    TopPayPresenter presenter;
    private WebView scroogeWebView;
    private ProgressBar progressBar;
    private PaymentPassData paymentPassData;
    private IPaymentModuleRouter paymentModuleRouter;

    private View btnBack;
    private View btnClose;
    private TextView tvTitle;
    private ProgressDialog progressDialog;

    public static final int REQUEST_CODE = 45675;
    private FingerPrintDialogPayment fingerPrintDialogPayment;
    private FingerprintDialogRegister fingerPrintDialogRegister;
    private boolean isInterceptOtp = true;
    private CommonWebViewClient webChromeWebviewClient;

    private String mJsHciCallbackFuncName;

    public static Intent createInstance(Context context, PaymentPassData paymentPassData) {
        Intent intent = new Intent(context, TopPayActivity.class);
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        return intent;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getResources().getColor(
                        R.color.tkpd_status_green_payment_module, null
                ));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(
                            R.color.tkpd_status_green_payment_module
                    ));
                }
            }
        }
        initInjector();
        if (getIntent().getExtras() != null) {
            setupBundlePass(getIntent().getExtras());
        }
        if (getIntent().getData() != null) {
            setupURIPass(getIntent().getData());
        }
        if (getApplication() instanceof IPaymentModuleRouter) {
            paymentModuleRouter = (IPaymentModuleRouter) getApplication();
        }
        initView();
        initVar();
        setViewListener();
        setActionVar();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initInjector() {
        DaggerFingerprintComponent
                .builder()
                .fingerprintModule(new FingerprintModule())
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
        presenter.attachView(this);
    }

    private void setActionVar() {
        presenter.proccessUriPayment();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setViewListener() {
        progressBar.setIndeterminate(true);

        WebSettings webSettings = scroogeWebView.getSettings();

        String userAgent = String.format("%s [%s/%s]", webSettings.getUserAgentString(), getString(R.string.app_android), GlobalConfig.VERSION_NAME);
        webSettings.setUserAgentString(userAgent);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(true);
        webSettings.setAppCacheEnabled(true);
        scroogeWebView.setWebViewClient(new TopPayWebViewClient());
        scroogeWebView.setWebChromeClient(webChromeWebviewClient);
        scroogeWebView.setOnKeyListener(getWebViewOnKeyListener());
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackPaymentCanceled();
            }
        });
    }

    private void initVar() {
        webChromeWebviewClient = new CommonWebViewClient(this, progressBar);
    }

    private void initView() {
        setContentView(R.layout.activity_top_pay_payment_module);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnBack = findViewById(R.id.btn_back);
        btnClose = findViewById(R.id.btn_close);
        scroogeWebView = (WebView) findViewById(R.id.scrooge_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.title_loading));
    }

    private void setupURIPass(Uri data) {
    }

    private void setupBundlePass(Bundle extras) {
        this.paymentPassData = extras.getParcelable(EXTRA_PARAMETER_TOP_PAY_DATA);
    }

    @Override
    public void renderWebViewPostUrl(String url, byte[] postData) {
        scroogeWebView.postUrl(url, postData);
    }

    @Override
    public void showToastMessageWithForceCloseView(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        callbackPaymentCanceled();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getStringFromResource(int resId) {
        return null;
    }

    @Override
    public PaymentPassData getPaymentPassData() {
        return paymentPassData;
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void hideProgressBar() {
        hideProgressLoading();
    }

    @Override
    public void showProgressBar() {
        showProgressLoading();
    }

    @Override
    public void showTimeoutErrorOnUiThread() {

    }

    @Override
    public void setWebPageTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void backStackAction() {
        onBackPressed();
    }

    public void closeView() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (paymentModuleRouter != null && paymentModuleRouter.getBaseUrlDomainPayment() != null
                && scroogeWebView.getUrl() != null
                && scroogeWebView.getUrl().contains(paymentModuleRouter.getBaseUrlDomainPayment())) {
            scroogeWebView.loadUrl("javascript:handlePopAndroid();");
        } else if (isEndThanksPage()) {
            callbackPaymentSucceed();
        } else {
            callbackPaymentCanceled();
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    public void callbackPaymentCanceled() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_CANCELLED, intent);
        finish();
    }

    public void callbackPaymentSucceed() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_SUCCESS, intent);
        finish();
    }

    public void callbackPaymentFailed() {
        hideProgressLoading();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData);
        setResult(PAYMENT_FAILED, intent);
        finish();
    }

    @Override
    public void onGoToOtpPage(String transactionId, String urlOtp) {
        fingerPrintDialogPayment.stopListening();
        fingerPrintDialogPayment.dismiss();
        presenter.getPostDataOtp(transactionId, urlOtp);
    }

    @Override
    public void onSuccessGetPostDataOTP(String postData, String urlOtp) {
        try {
            isInterceptOtp = false;
            scroogeWebView.postUrl(urlOtp, postData.getBytes(CHARSET_UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorGetPostDataOtp(Throwable e) {
        NetworkErrorHelper.showSnackbar(this, ErrorHandler.getErrorMessage(this, e));
    }

    @Override
    public void onPaymentFingerPrint(String transactionId, String publicKey, String date, String signature, String userId) {
        presenter.paymentFingerPrint(transactionId, publicKey, date, signature, userId);
    }

    @Override
    public void onRegisterFingerPrint(String transactionId, String publicKey, String date, String signature, String userId) {
        presenter.registerFingerPrint(transactionId, publicKey, date, signature, userId);
    }

    private class TopPayWebViewClient extends WebViewClient {
        private boolean timeout = true;

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //Log.d(TAG, "redirect url = " + url);

            if (!url.isEmpty() && url.contains(PaymentFingerprintConstant.APP_LINK_FINGERPRINT) &&
                    paymentModuleRouter.getEnableFingerprintPayment()) {
                Uri uri = Uri.parse(url);
                String transactionId = uri.getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID);
                fingerPrintDialogRegister = FingerprintDialogRegister.createInstance(presenter.getUserId(), transactionId);
                fingerPrintDialogRegister.setListenerRegister(TopPayActivity.this);
                fingerPrintDialogRegister.setContext(TopPayActivity.this);
                fingerPrintDialogRegister.show(getSupportFragmentManager(), "fingerprintRegister");
                return true;
            }

            /*
              HANYA SEMENTARA HARCODE, NANTI PAKAI APPLINK
             */
            if (!url.isEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_LIVE
                    + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY)
                    || url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_STAGING
                    + Constant.TempRedirectPayment.TOP_PAY_PATH_HELP_URL_TEMPORARY))) {
                String deepLinkUrl = Constant.TempRedirectPayment.APP_LINK_SCHEME_WEB_VIEW
                        + "?url=" + URLEncoder.encode(url);
                RouteManager.route(TopPayActivity.this, deepLinkUrl);
                return true;
            } else {
//                if (!url.isEmpty() && (url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_LIVE) ||
//                        url.contains(Constant.TempRedirectPayment.TOP_PAY_DOMAIN_URL_STAGING))) {
//                    paymentModuleRouter.actionAppLinkPaymentModule(
//                            TopPayActivity.this, Constant.TempRedirectPayment.APP_LINK_SCHEME_HOME
//                    );
//                    return true;
//                }
            }

            if (!TextUtils.isEmpty(paymentPassData.getCallbackSuccessUrl()) &&
                    url.contains(paymentPassData.getCallbackSuccessUrl())) {
                view.stopLoading();
                callbackPaymentSucceed();
                return true;
            } else if (!TextUtils.isEmpty(paymentPassData.getCallbackFailedUrl()) &&
                    url.contains(paymentPassData.getCallbackFailedUrl())) {
                view.stopLoading();
                callbackPaymentFailed();
                return true;
            } else if (url.contains(ACCOUNTS_URL)) {
                view.stopLoading();
                processRedirectUrlContaintsAccountsUrl(url);
                return true;
            } else if (url.contains(LOGIN_URL)) {
                view.stopLoading();
                showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TOPPAY);
                return true;
            } else if (url.contains(HCI_CAMERA_KTP)) {
                view.stopLoading();
                mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
                startActivityForResult(RouteManager.getIntent(TopPayActivity.this, ApplinkConst.HOME_CREDIT_KTP), HCI_CAMERA_REQUEST_CODE);
                return true;
            } else if (url.contains(HCI_CAMERA_SELFIE)) {
                view.stopLoading();
                mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
                startActivityForResult(RouteManager.getIntent(TopPayActivity.this, ApplinkConst.HOME_CREDIT_SELFIE), HCI_CAMERA_REQUEST_CODE);
                return true;
            } else {
                if (ApplinkConst.PAYMENT_BACK_TO_DEFAULT.equalsIgnoreCase(url)) {
                    if (isEndThanksPage()) callbackPaymentSucceed();
                    else callbackPaymentCanceled();
                    return true;
                } else if (RouteManager.isSupportApplink(TopPayActivity.this, url)) {
                    //  RouteManager.route(TopPayActivity.this, url);
                    Intent intent = RouteManager.getIntent(TopPayActivity.this, url);
                    intent.setData(Uri.parse(url));
                    navigateToActivity(intent);
                    return true;
                } else {
                    if (paymentModuleRouter != null) {
                        String urlFinal = paymentModuleRouter.getGeneratedOverrideRedirectUrlPayment(url);
                        if (urlFinal == null) {
                            return super.shouldOverrideUrlLoading(view, url);
                        } else {
                            view.loadUrl(
                                    urlFinal,
                                    paymentModuleRouter.getGeneratedOverrideRedirectHeaderUrlPayment(urlFinal)
                            );
                            return true;
                        }
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest request) {
            if ((request.getUrl().toString().contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_SPRINTASIA) ||
                    request.getUrl().toString().contains(PaymentFingerprintConstant.TOP_PAY_PATH_CREDIT_CARD_VERITRANS)) && isInterceptOtp &&
                    request.getUrl().getQueryParameter(PaymentFingerprintConstant.ENABLE_FINGERPRINT).equalsIgnoreCase("true") &&
                    paymentModuleRouter.getEnableFingerprintPayment()) {
                fingerPrintDialogPayment = FingerPrintDialogPayment.createInstance(presenter.getUserId(), request.getUrl().toString(),
                        request.getUrl().getQueryParameter(PaymentFingerprintConstant.TRANSACTION_ID));
                fingerPrintDialogPayment.setListenerPayment(TopPayActivity.this);
                fingerPrintDialogPayment.setContext(TopPayActivity.this);
                fingerPrintDialogPayment.show(getSupportFragmentManager(), "fingerprintPayment");
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.stopLoading();
                    }
                });
            }
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
            //  Log.d(TAG, "start url = " + url);
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
                                showErrorTimeout(view);
                            }
                        });
                    }
                }
            }).start();
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
        }

        private void showErrorTimeout(WebView view) {
            view.stopLoading();
            showToastMessageWithForceCloseView(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        }
    }

    private void processRedirectUrlContaintsAccountsUrl(String url) {
        Uri uriMain = Uri.parse(url);
        String ld = uriMain.getQueryParameter(KEY_QUERY_LD);
        String urlThanks;
        try {
            urlThanks = URLDecoder.decode(ld, CHARSET_UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            urlThanks = "";
        }
        Uri uri = Uri.parse(urlThanks);
        String paymentId = uri.getQueryParameter(KEY_QUERY_PAYMENT_ID);
        if (paymentId != null)
            callbackPaymentSucceed();
        else
            callbackPaymentFailed();
    }

    private class TopPayWebViewChromeClient extends WebChromeClient {

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

    public boolean isEndThanksPage() {
        if (scroogeWebView.getUrl() == null || scroogeWebView.getUrl().isEmpty()) return false;
        for (String thanksUrl : THANK_PAGE_URL_LIST) {
            if (scroogeWebView.getUrl().contains(thanksUrl)) {
                return true;
            }
        }
        return false;
    }

    private View.OnKeyListener getWebViewOnKeyListener() {
        return new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            onBackPressed();
                            return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    protected void onPause() {
        if (fingerPrintDialogPayment != null) {
            fingerPrintDialogPayment.stopListening();
        }
        if (fingerPrintDialogRegister != null) {
            fingerPrintDialogRegister.stopListening();
        }
        super.onPause();
    }

    public void hideProgressLoading() {
        progressBar.setVisibility(View.GONE);
    }

    public void showProgressLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessRegisterFingerPrint() {
        fingerPrintDialogRegister.stopListening();
        fingerPrintDialogRegister.dismiss();
        NetworkErrorHelper.showGreenCloseSnackbar(this, getString(R.string.fingerprint_label_successed_fingerprint));
    }

    @Override
    public void hideProgressBarDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onErrorRegisterFingerPrint(Throwable e) {
        fingerPrintDialogRegister.onErrorRegisterFingerPrint();
    }

    @Override
    public void showErrorRegisterSnackbar() {
        NetworkErrorHelper.showRedCloseSnackbar(this, getString(R.string.fingerprint_label_failed_fingerprint));
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void onErrorPaymentFingerPrint(Throwable e) {
        fingerPrintDialogPayment.onErrorNetworkPaymentFingerPrint();
    }

    @Override
    public void onSuccessPaymentFingerprint(String url, String paramEncode) {
        fingerPrintDialogPayment.stopListening();
        fingerPrintDialogPayment.dismiss();
        scroogeWebView.loadUrl(String.format("%1$s?%2$s", url, paramEncode));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == CommonWebViewClient.ATTACH_FILE_REQUEST && webChromeWebviewClient != null) {
            webChromeWebviewClient.onActivityResult(requestCode, resultCode, intent);
        } else if (requestCode == HCI_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            String imagePath = intent.getStringExtra(HCI_KTP_IMAGE_PATH);
            String base64 = encodeToBase64(imagePath);
            if (imagePath != null) {
                StringBuilder jsCallbackBuilder = new StringBuilder();
                jsCallbackBuilder.append("javascript:")
                        .append(mJsHciCallbackFuncName)
                        .append("('")
                        .append(imagePath)
                        .append("'")
                        .append(", ")
                        .append("'")
                        .append(base64)
                        .append("')");
                scroogeWebView.loadUrl(jsCallbackBuilder.toString());
            }
        }
    }

    public static String encodeToBase64(String imagePath) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
