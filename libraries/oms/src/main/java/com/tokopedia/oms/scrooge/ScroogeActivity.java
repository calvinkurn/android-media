package com.tokopedia.oms.scrooge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.webview.CommonWebViewClient;
import com.tokopedia.abstraction.base.view.webview.FilePickerInterface;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.oms.R;

import java.io.ByteArrayOutputStream;

public class ScroogeActivity extends AppCompatActivity implements FilePickerInterface {
    //callbacks URL's
    private static String ADD_CC_SUCESS_CALLBACK = "tokopedia://action_add_cc_success";
    private static String ADD_CC_FAIL_CALLBACK = "tokopedia://action_add_cc_fail";
    private static String DELETE_CC_SUCESS_CALLBACK = "tokopedia://action_delete_cc_success";
    private static String DELETE_CC_FAIL_CALLBACK = "tokopedia://action_delete_cc_fail";
    private static String SUCCESS_CALLBACK = "tokopedia://order/";

    private static final String EXTRA_KEY_POST_PARAMS = "EXTRA_KEY_POST_PARAMS";
    private static final String EXTRA_KEY_URL = "URL";
    private static final String EXTRA_IS_POST_REQUEST = "EXTRA_IS_POST_REQUEST";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    private static final String HCI_CAMERA_KTP = "android-js-call://ktp";
    private static final String HCI_CAMERA_SELFIE = "android-js-call://selfie";
    private static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    public static final int HCI_CAMERA_REQUEST_CODE = 978;

    private WebView mWebView;
    private ProgressBar mProgress;
    private Toolbar mToolbar;
    private String mPostParams;
    private String mURl;

    private int requestCode;
    private boolean isPostRequest;
    private String title;
    private CommonWebViewClient webChromeWebviewClient;
    private String mJsHciCallbackFuncName;

    public static Intent getCallingIntent(Context context, String url, boolean isPostRequest, String postParams, String title) {
        Intent intent = new Intent(context, ScroogeActivity.class);
        intent.putExtra(EXTRA_KEY_POST_PARAMS, postParams);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_IS_POST_REQUEST, isPostRequest);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostParams = getIntent().getStringExtra(EXTRA_KEY_POST_PARAMS);
        mURl = getIntent().getStringExtra(EXTRA_KEY_URL);
        isPostRequest = getIntent().getBooleanExtra(EXTRA_IS_POST_REQUEST, false);
        title = getIntent().getStringExtra(EXTRA_TITLE);

        setContentView(R.layout.activity_scrooge_web_view);

        initUI();

        if (mPostParams == null) mPostParams = "";
        this.mWebView.postUrl(mURl, mPostParams.getBytes());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initUI() {
        mWebView = (WebView) findViewById(R.id.webview);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);

        //setup toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            invalidateOptionsMenu();
        }

        mProgress.setIndeterminate(true);

        webChromeWebviewClient = new CommonWebViewClient(this, mProgress);
        setupWebView(this.mWebView);
    }

    /**
     * Set up web view and register webview client
     *
     * @param webview
     */
    private void setupWebView(WebView webview) {
        if (webview == null) return;

        webview.setWebViewClient(new WebViewClient() {
            public synchronized void onPageStarted(WebView inView, String inUrl, Bitmap inFavicon) {
                super.onPageStarted(inView, inUrl, inFavicon);
                CommonUtils.dumper("ScroogeActivity :: onPageStarted url " + inUrl);

                try {
                    setProgressBarIndeterminateVisibility(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mProgress.setVisibility(View.VISIBLE);
            }

            public synchronized void onPageFinished(WebView inView, String inUrl) {
                super.onPageFinished(inView, inUrl);
                CommonUtils.dumper("ScroogeActivity :: onPageFinished url " + inUrl);
            }

            public synchronized void onReceivedError(WebView inView, int iniErrorCode, String inDescription, String inFailingUrl) {
                super.onReceivedError(inView, iniErrorCode, inDescription, inFailingUrl);
                CommonUtils.dumper("ScroogeActivity :: Error occured while loading url " + inFailingUrl);
                Intent responseIntent = new Intent();
                responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, inDescription);
                setResult(ScroogePGUtil.RESULT_CODE_RECIEVED_ERROR, responseIntent);
                finish();
            }

            public synchronized void onReceivedSslError(WebView inView, SslErrorHandler inHandler, SslError inError) {
                CommonUtils.dumper("ScroogeActivity :: SSL Error occured " + inError.toString());
                CommonUtils.dumper("ScroogeActivity :: SSL Handler is " + inHandler);
                if (inHandler != null) {
                    inHandler.proceed();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent responseIntent = new Intent();

                boolean returnVal = true;
                if (url.equalsIgnoreCase(ADD_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_SUCCESS, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(ADD_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Fail");
                    setResult(ScroogePGUtil.RESULT_CODE_ADD_CC_FAIL, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(DELETE_CC_FAIL_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_FAIL, responseIntent);
                    finish();
                } else if (url.equalsIgnoreCase(DELETE_CC_SUCESS_CALLBACK)) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "FAIL");
                    setResult(ScroogePGUtil.RESULT_CODE_DELETE_CC_SUCCESS, responseIntent);
                    finish();
                } else if (url.startsWith(SUCCESS_CALLBACK) && url.substring(0, url.lastIndexOf("/")).endsWith("order")) {
                    responseIntent.putExtra(ScroogePGUtil.RESULT_EXTRA_MSG, "Success");
                    responseIntent.putExtra(ScroogePGUtil.SUCCESS_MSG_URL, url);
                    setResult(ScroogePGUtil.RESULT_CODE_SUCCESS, responseIntent);
                    finish();
                } else if (url.contains(HCI_CAMERA_KTP)) {
                    view.stopLoading();
                    mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
                    startActivityForResult(RouteManager.getIntent(ScroogeActivity.this, ApplinkConst.HOME_CREDIT_KTP), HCI_CAMERA_REQUEST_CODE);
                    return true;
                } else if (url.contains(HCI_CAMERA_SELFIE)) {
                    view.stopLoading();
                    mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
                    startActivityForResult(RouteManager.getIntent(ScroogeActivity.this, ApplinkConst.HOME_CREDIT_SELFIE), HCI_CAMERA_REQUEST_CODE);
                    return true;
                } else {
                    super.shouldOverrideUrlLoading(view, url);
                    returnVal = true;
                }

                return returnVal;
            }
        });

        webview.setWebChromeClient(webChromeWebviewClient);
        webview.getSettings().setJavaScriptEnabled(true);
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
                mWebView.loadUrl(jsCallbackBuilder.toString());
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
