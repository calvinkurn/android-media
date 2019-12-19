package com.tokopedia.webview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.RouteManagerKt;
import com.tokopedia.network.utils.URLGenerator;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.webview.ext.UrlEncoderExtKt;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.abstraction.common.utils.image.ImageHandler.encodeToBase64;
import static com.tokopedia.webview.ConstantKt.JS_TOKOPEDIA;
import static com.tokopedia.webview.ConstantKt.KEY_ALLOW_OVERRIDE;
import static com.tokopedia.webview.ConstantKt.KEY_NEED_LOGIN;
import static com.tokopedia.webview.ConstantKt.KEY_URL;
import static com.tokopedia.webview.ConstantKt.SEAMLESS;
import static com.tokopedia.webview.ext.UrlEncoderExtKt.encodeOnce;


public abstract class BaseWebViewFragment extends BaseDaggerFragment {

    private static final int MAX_PROGRESS = 100;
    private static final int HALF_PROGRESS = 50;
    private static final int PICTURE_QUALITY = 60;

    public TkpdWebView webView;
    ProgressBar progressBar;
    private ValueCallback<Uri> uploadMessageBeforeLolipop;
    public ValueCallback<Uri[]> uploadMessageAfterLolipop;
    public final static int ATTACH_FILE_REQUEST = 1;
    private static final String HCI_CAMERA_KTP = "android-js-call://ktp";
    private static final String HCI_CAMERA_SELFIE = "android-js-call://selfie";
    String mJsHciCallbackFuncName;
    public static final int HCI_CAMERA_REQUEST_CODE = 978;
    private static final int REQUEST_CODE_LOGIN = 1233;
    private static final int LOGIN_GPLUS = 458;
    private static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    private static final String KOL_URL = "tokopedia.com/content";
    private static final String PLAY_GOOGLE_URL = "play.google.com";
    private static final String PARAM_EXTERNAL = "tokopedia_external=true";
    private static final String PARAM_WEBVIEW_BACK = "tokopedia://back";

    @NonNull
    protected String url = "";
    public static final String TOKOPEDIA_STRING = "tokopedia";
    protected boolean isTokopediaUrl;
    boolean allowOverride = true;
    private boolean needLogin = false;

    // last check overrideUrlLoading
    // true means it has move to native page
    boolean hasMoveToNativePage = false;

    // check if webview load is greater than threshold (50%)
    boolean webViewHasContent = false;

    private UserSession userSession;

    /**
     * return the url to load in the webview
     * You can use URLGenerator.java to use generate the seamless URL.
     */
    @NonNull
    protected String getUrl() {
        if (url.contains(JS_TOKOPEDIA)) {
            return url;
        } else if (isTokopediaUrl) {
            String gcmId = userSession.getDeviceId();
            String userId = userSession.getUserId();
            return URLGenerator.generateURLSessionLogin(
                    encodeOnce(url),
                    gcmId,
                    userId);
        } else {
            return url;
        }
    }

    @Override
    protected void initInjector() {
        // noop
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(getContext());
        Bundle args = getArguments();
        if (args == null || !args.containsKey(KEY_URL)) {
            return;
        }
        url = UrlEncoderExtKt.decode(args.getString(KEY_URL, TokopediaUrl.Companion.getInstance().getWEB()));
        needLogin = args.getBoolean(KEY_NEED_LOGIN, false);
        allowOverride = args.getBoolean(KEY_ALLOW_OVERRIDE, true);
        String host = Uri.parse(url).getHost();
        isTokopediaUrl = host != null && host.contains(TOKOPEDIA_STRING);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean overrideUrlLoading = shouldOverrideUrlLoading(webView, url);
        if (overrideUrlLoading) {
            hasMoveToNativePage = true;
            getActivity().finish();
            return null;
        } else {
            return onCreateWebView(inflater, container, savedInstanceState);
        }
    }

    private View onCreateWebView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        webView = view.findViewById(setWebView());
        progressBar = view.findViewById(setProgressBar());

        CookieManager.getInstance().setAcceptCookie(true);

        webView.clearCache(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }
        return view;
    }

    protected int getLayout() {
        return R.layout.fragment_general_web_view_lib;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setIndeterminate(true);

        if (needLogin && !userSession.isLoggedIn()) {
            startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
        } else {
            if (!TextUtils.isEmpty(url)) {
                loadWeb();
            }
        }
    }

    protected void loadWeb() {
        if (isTokopediaUrl) {
            webView.loadAuthUrl(getUrl(), new UserSession(getContext()));
        } else {
            webView.loadAuthUrl(getUrl(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == HCI_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            String imagePath = intent.getStringExtra(HCI_KTP_IMAGE_PATH);
            String base64 = encodeToBase64(imagePath, PICTURE_QUALITY);
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
                webView.loadUrl(jsCallbackBuilder.toString());
            }
        }
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
            if (uploadMessageAfterLolipop != null) {
                uploadMessageAfterLolipop.onReceiveValue(results);
                uploadMessageAfterLolipop = null;
            }
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == uploadMessageBeforeLolipop) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                uploadMessageBeforeLolipop.onReceiveValue(result);
                uploadMessageBeforeLolipop = null;
            }
        }

        if (requestCode == REQUEST_CODE_LOGIN) {
            webView.loadAuthUrl(getUrl(), userSession);
        } else if (requestCode == LOGIN_GPLUS) {
            String historyUrl = "";
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0)
                historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
            if (historyUrl.contains(SEAMLESS)){
                webView.loadAuthUrl(historyUrl, null);
            }
            else {
                webView.loadAuthUrl(historyUrl, userSession);
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == MAX_PROGRESS) {
                onLoadFinished();
            }
            if (newProgress >= HALF_PROGRESS && !webViewHasContent) {
                webViewHasContent = true;
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

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Activity activity = getActivity();
            if (activity instanceof AppCompatActivity) {
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    String decodedUrl = Uri.decode(url).toLowerCase();
                    if (!TextUtils.isEmpty(title)
                            && Uri.parse(title).getScheme() == null
                            && isKolUrl(decodedUrl)) {
                        actionBar.setTitle(title);
                    } else {
                        actionBar.setTitle(getString(R.string.tokopedia));
                    }
                }
            }
        }

        private boolean isKolUrl(String url) {
            return url.contains(KOL_URL);
        }
    }

    void openFileChooserBeforeLolipop(ValueCallback<Uri> uploadMessage) {
        uploadMessageBeforeLolipop = uploadMessage;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            try {
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String requestUrl) {
            if (hasCheckOverrideAtInitialization(requestUrl)) return false;
            boolean overrideUrl = BaseWebViewFragment.this.shouldOverrideUrlLoading(view, requestUrl);
            checkActivityFinish();
            return overrideUrl;
        }

        @RequiresApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(webView, request.getUrl().toString());
        }

        boolean hasCheckOverrideAtInitialization(String requestUrl){
            return (allowOverride && requestUrl.equals(url));
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }
    }

    protected boolean shouldOverrideUrlLoading(@Nullable WebView webview, @NonNull String url) {
        if (getActivity() == null) {
            return false;
        }
        if (goToLoginGoogle(url)) return true;

        if (url.contains(HCI_CAMERA_KTP)) {
            mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
            startActivityForResult(RouteManager.getIntent(getActivity(), ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE), HCI_CAMERA_REQUEST_CODE);
            return true;
        } else if (url.contains(HCI_CAMERA_SELFIE)) {
            mJsHciCallbackFuncName = Uri.parse(url).getLastPathSegment();
            startActivityForResult(RouteManager.getIntent(getActivity(), ApplinkConst.HOME_CREDIT_SELFIE_WITHOUT_TYPE), HCI_CAMERA_REQUEST_CODE);
            return true;
        } else if (PARAM_WEBVIEW_BACK.equalsIgnoreCase(url)
                && getActivity()!= null) {
            if (getActivity().isTaskRoot()) {
                getActivity().finish();
            } else {
                RouteManager.route(getContext(), ApplinkConst.HOME);
            }
            return true;
        } else if (url.contains(PLAY_GOOGLE_URL)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
        if (!allowOverride) {
            return false;
        }
        if (url.contains(PARAM_EXTERNAL)) {
            try {
                Intent destination = new Intent(Intent.ACTION_VIEW);
                destination.setData(Uri.parse(url));
                startActivity(destination);
                hasMoveToNativePage = true;
                return true;
            } catch (ActivityNotFoundException e) {
                return false;
            }
        }
        boolean isNotNetworkUrl = !URLUtil.isNetworkUrl(url);
        if (isNotNetworkUrl) {
            Intent intent = RouteManager.getIntentNoFallback(getActivity(), url);
            if (intent!= null) {
                hasMoveToNativePage = true;
                startActivity(intent);
                return true;
            } else {
                // logging here, url might return blank page
                // ask user to update app
            }
        }
        hasMoveToNativePage = RouteManagerKt.moveToNativePageFromWebView(getActivity(), url);
        return hasMoveToNativePage;
    }

    private void checkActivityFinish(){
        if (hasMoveToNativePage && !webViewHasContent && getActivity()!= null) {
            getActivity().finish();
        }
    }

    private boolean goToLoginGoogle(@NonNull String url){
        String loginType;
        try {
            loginType = Uri.parse(url).getQueryParameter("login_type");
        } catch (Exception e) {
            return false;
        }
        if ("plus".equals(loginType)) {
            Intent intent = RouteManager.getIntentNoFallback(getActivity(), ApplinkConst.LOGIN);
            if (intent != null) {
                intent.putExtra("auto_login", true);
                intent.putExtra("method", 222);
                startActivityForResult(intent, LOGIN_GPLUS);
            }
            return true;
        }
        return false;
    }

    protected void onLoadFinished() {
        // webview is blank, but it already goes to native page, so this activity is safe to close.
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public TkpdWebView getWebView() {
        return webView;
    }

    public int setWebView(){
        return R.id.webview;
    }

    public int setProgressBar() {
        return R.id.progressbar;
    }

    public void reloadPage(){
        webView.reload();
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
