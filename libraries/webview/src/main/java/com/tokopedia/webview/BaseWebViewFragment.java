package com.tokopedia.webview;

import static android.app.Activity.RESULT_OK;
import static com.tokopedia.webview.ConstantKt.DEFAULT_TITLE;
import static com.tokopedia.webview.ConstantKt.GOOGLE_DOCS_PDF_URL;
import static com.tokopedia.webview.ConstantKt.JS_STAGING_TOKOPEDIA;
import static com.tokopedia.webview.ConstantKt.JS_TOKOPEDIA;
import static com.tokopedia.webview.ConstantKt.KEY_ALLOW_OVERRIDE;
import static com.tokopedia.webview.ConstantKt.KEY_NEED_LOGIN;
import static com.tokopedia.webview.ConstantKt.KEY_PULL_TO_REFRESH;
import static com.tokopedia.webview.ConstantKt.KEY_URL;
import static com.tokopedia.webview.ConstantKt.LOGIN;
import static com.tokopedia.webview.ConstantKt.PARAM_EXTERNAL_TRUE;
import static com.tokopedia.webview.ConstantKt.SEAMLESS;
import static com.tokopedia.webview.ConstantKt.STAGING;
import static com.tokopedia.webview.ConstantKt.WWW_TOKOPEDIA_COM;
import static com.tokopedia.webview.ConstantKt.ZOOM_US_STRING;
import static com.tokopedia.webview.ext.UrlEncoderExtKt.decode;
import static com.tokopedia.webview.ext.UrlEncoderExtKt.encodeOnce;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.RouteManagerKt;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.globalerror.GlobalError;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.locationmanager.RequestLocationType;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.network.utils.URLGenerator;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.webview.ext.UrlEncoderExtKt;
import com.tokopedia.webview.jsinterface.PrintWebPageInterface;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;


public abstract class BaseWebViewFragment extends BaseDaggerFragment {

    private static final int MAX_PROGRESS = 100;
    private static final int HALF_PROGRESS = 50;
    private static final int PICTURE_QUALITY = 60;
    private static final String ERR_INTERNET_DISCONNECTED = "ERR_INTERNET_DISCONNECTED";
    public static final String TOKOPEDIA_COM = ".tokopedia.com";

    public TkpdWebView webView;
    ProgressBar progressBar;
    private SwipeToRefresh swipeRefreshLayout;
    private GlobalError globalError;
    private ValueCallback<Uri> uploadMessageBeforeLolipop;
    public ValueCallback<Uri[]> uploadMessageAfterLolipop;
    public final static int ATTACH_FILE_REQUEST = 1;
    private static final String HCI_CAMERA_KTP = "android-js-call://ktp";
    private static final String HCI_CAMERA_SELFIE = "android-js-call://selfie";
    private static final String LOGIN_APPLINK = "tokopedia://login";
    private static final String RESOLUTION_APPLINK = "tokopedia://resolution/success-create";
    private static final String REGISTER_APPLINK = "tokopedia://registration";
    private static final String CLEAR_CACHE_PREFIX = "/clear-cache";
    private static final String KEY_CLEAR_CACHE = "android_webview_clear_cache";
    private static final String KEY_ENABLE_WEBVIEW_PRINT_JS_INTERFACE = "android_enable_webview_print_jsinterface";
    private static final String LINK_AJA_APP_LINK = "https://linkaja.id/applink/payment";
    private static final String GOJEK_APP_LINK = "https://gojek.link/goclub/membership?source=toko_status_match";

    String mJsHciCallbackFuncName;
    public static final int HCI_CAMERA_REQUEST_CODE = 978;
    private static final int REQUEST_CODE_LOGIN = 1233;
    private static final int REQUEST_CODE_LOGOUT = 1234;
    private static final int REQUEST_CODE_LIVENESS = 1235;
    private static final int LOGIN_GPLUS = 458;
    private static final String HCI_KTP_IMAGE_PATH = "ktp_image_path";
    private static final String KOL_URL = "tokopedia.com/content";
    private static final String PRINT_AWB_URL = "tokopedia.com/shipping-label";
    private static final String PLAY_GOOGLE_URL = "play.google.com";
    private static final String BRANCH_IO_HOST = "tokopedia.link";
    private static final String FDL_HOST = "tkpd.page.link";
    private static String ENABLE_FDL_HOST_WEBVIEW = "android_enable_fdl_host_webview";
    private static final String SCHEME_INTENT = "intent";
    private static final String PARAM_WEBVIEW_BACK = "tokopedia://back";
    public static final String CUST_OVERLAY_URL = "imgurl";
    private static final String CUST_HEADER = "header_text";
    private static final String HELP_URL = "tokopedia.com/help";
    private static final String ANDROID_PRINT_JS_INTERFACE = "AndroidPrint";

    @NonNull
    protected String url = "";
    public static final String TOKOPEDIA_STRING = "tokopedia";
    protected boolean isTokopediaUrl;
    boolean allowOverride = true;
    boolean pullToRefresh = false;
    private boolean needLogin = false;

    // last check overrideUrlLoading
    // true means it has move to native page
    boolean hasMoveToNativePage = false;

    // check if webview load is greater than threshold (50%)
    boolean webViewHasContent = false;

    private UserSession userSession;
    private PermissionCheckerHelper permissionCheckerHelper;
    private RemoteConfig remoteConfig;

    /**
     * return the url to load in the webview
     * You can use URLGenerator.java to use generate the seamless URL.
     */
    @NonNull
    protected String getUrl() {
        String env = TokopediaUrl.Companion.getInstance().getTYPE().getValue();
        if (url.contains(JS_TOKOPEDIA) || (url.contains(JS_STAGING_TOKOPEDIA) && env.equalsIgnoreCase(STAGING))) {
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
        url = getUrlFromArguments(args);
        needLogin = args.getBoolean(KEY_NEED_LOGIN, false);
        allowOverride = args.getBoolean(KEY_ALLOW_OVERRIDE, true);
        pullToRefresh = args.getBoolean(KEY_PULL_TO_REFRESH, false);
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        if (uri.getUserInfo() != null) {
            ErrorHandler.Builder builder = new ErrorHandler.Builder();
            builder.sendToScalyr(true);
            builder.setErrorCode(true);
            Toast.makeText(getActivity(), ErrorHandler.getErrorMessage(getActivity(), new NullPointerException("Unable to open link"), builder), Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        isTokopediaUrl = host != null && host.endsWith(TOKOPEDIA_COM) && !host.contains(ZOOM_US_STRING);
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
    }

    private String getUrlFromArguments(Bundle args) {
        String defaultUrl = TokopediaUrl.Companion.getInstance().getWEB();
        String url = decode(args.getString(KEY_URL, defaultUrl));

        if (!url.startsWith("http")) {
            return defaultUrl;
        }
        return url;
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
            try {
                return onCreateWebView(inflater, container, savedInstanceState);
            } catch (Exception e) {
                redirectToWebViewPlaystore();
                return null;
            }
        }
    }

    private void redirectToWebViewPlaystore() {
        //fix crash Failed to load WebView provider: No WebView installed
        Intent webViewPlaystoreIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.webview")
        );
        Toast.makeText(getContext(), getString(com.tokopedia.webview.R.string.webview_need_install),
                Toast.LENGTH_LONG).show();
        startActivity(webViewPlaystoreIntent);
        getActivity().finish();
    }

    private View onCreateWebView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        webView = view.findViewById(setWebView());
        progressBar = view.findViewById(setProgressBar());
        swipeRefreshLayout = view.findViewById(R.id.general_web_view_lib_swipe_refresh_layout);
        globalError = view.findViewById(R.id.unify_global_error);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(pullToRefresh);
            swipeRefreshLayout.setOnRefreshListener(this::reloadPage);
        }

        Boolean isEnablePrintJsInterface = remoteConfig.getBoolean(KEY_ENABLE_WEBVIEW_PRINT_JS_INTERFACE, false);

        webView.clearCache(true);
        webView.addJavascriptInterface(new WebToastInterface(getActivity()), "Android");
        if (isEnablePrintJsInterface) {
            webView.addJavascriptInterface(new PrintWebPageInterface(getActivity(), webView), ANDROID_PRINT_JS_INTERFACE);
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString(webSettings.getUserAgentString() + " Mobile webview ");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webSettings.setMediaPlaybackRequiresUserGesture(false);

        if (GlobalConfig.isAllowDebuggingTools()) {
            webView.setWebContentsDebuggingEnabled(true);
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
        String url = getUrl();
        if (isTokopediaUrl) {
            webView.requestFocus();
            webView.loadAuthUrl(url, new UserSession(getContext()));
        } else {
            redirectToNativeBrowser();
        }
    }

    private void redirectToNativeBrowser() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
            getActivity().finish();
        } catch (Throwable th) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "browser");
            messageMap.put("url", url);
            ServerLogger.log(Priority.P1, "INVALID_WEBVIEW_URL", messageMap);
        }
    }


    private final class WebToastInterface {

        private WeakReference<Activity> mContextRef;
        private Toast toast;

        public WebToastInterface(Activity context) {
            this.mContextRef = new WeakReference<Activity>(context);
        }

        @JavascriptInterface
        public void showToast(final String toastMsg) {

            if (TextUtils.isEmpty(toastMsg) || mContextRef.get() == null) {
                return;
            }
            mContextRef.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(mContextRef.get(), toastMsg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_LIVENESS && resultCode == RESULT_OK) {
            String kycRedirectionUrl = intent.getStringExtra(ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL);
            webView.loadUrl(kycRedirectionUrl);
            return;
        }

        if (requestCode == HCI_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            String imagePath = intent.getStringExtra(HCI_KTP_IMAGE_PATH);
            String base64 = encodeToBase64(imagePath, PICTURE_QUALITY);
            if (imagePath != null && base64 != null) {
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
            if (resultCode == RESULT_OK) {
                webView.loadAuthUrl(getUrl(), userSession);
            } else {
                if (getActivity() != null && getActivity() instanceof BaseSimpleWebViewActivity)
                    ((BaseSimpleWebViewActivity) getActivity()).goPreviousActivity();
            }
        } else if (requestCode == LOGIN_GPLUS) {
            String historyUrl = "";
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0)
                historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
            if (historyUrl.contains(SEAMLESS)) {
                webView.loadAuthUrl(historyUrl, null);
            } else {
                webView.loadAuthUrl(historyUrl, userSession);
            }
        }

        if (requestCode == REQUEST_CODE_LOGOUT && resultCode == RESULT_OK) {
            hasMoveToNativePage = true;
            startActivity(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN));
        }
    }

    public static @Nullable
    String encodeToBase64(String imagePath, int quality) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            checkLocationPermission(callback, origin);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            for (String resource :
                    request.getResources()) {
                if (resource.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                    permissionCheckerHelper = new PermissionCheckerHelper();
                    permissionCheckerHelper.checkPermission(BaseWebViewFragment.this, PermissionCheckerHelper.Companion.PERMISSION_CAMERA, new PermissionCheckerHelper.PermissionCheckListener() {
                        @Override
                        public void onPermissionDenied(String permissionText) {
                            request.deny();
                        }

                        @Override
                        public void onNeverAskAgain(String permissionText) {
                            request.deny();
                        }

                        @Override
                        public void onPermissionGranted() {
                            request.grant(request.getResources());
                        }
                    }, getString(R.string.need_permission_camera));
                }
            }
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            super.onPermissionRequestCanceled(request);
        }

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
            if (activity instanceof BaseSimpleWebViewActivity) {
                BaseSimpleWebViewActivity activityInstance = (BaseSimpleWebViewActivity) activity;
                String activityTitle = activityInstance.getWebViewTitle();

                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    String decodedUrl = Uri.decode(url).toLowerCase();
                    if (!TextUtils.isEmpty(title)
                            && Uri.parse(title).getScheme() == null
                            && (isKolUrl(decodedUrl) || isPrintAwbUrl(decodedUrl))) {
                        actionBar.setTitle(title);
                    } else if ((TextUtils.isEmpty(activityTitle) || activityTitle.equals(DEFAULT_TITLE))
                            && !isHelpUrl(decodedUrl)) {
                        actionBar.setTitle(getString(R.string.tokopedia));
                    }
                }
            }
        }

        private boolean isPrintAwbUrl(String url) {
            return url.contains(PRINT_AWB_URL);
        }

        private boolean isKolUrl(String url) {
            return url.contains(KOL_URL);
        }
    }

    private void checkLocationPermission(GeolocationPermissions.Callback callback, String origin) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            permissionCheckerHelper = new PermissionCheckerHelper();

            permissionCheckerHelper.checkPermissions(this,
                    LocationDetectorHelper.Companion.getPermissions(RequestLocationType.APPROXIMATE_OR_PRECISE),
                    new PermissionCheckerHelper.PermissionCheckListener() {
                        @Override
                        public void onPermissionDenied(String permissionText) {
                            callback.invoke(origin, false, false);
                        }

                        @Override
                        public void onNeverAskAgain(String permissionText) {
                            callback.invoke(origin, false, false);
                        }

                        @Override
                        public void onPermissionGranted() {
                            callback.invoke(origin, true, false);
                        }
                    }, getString(R.string.webview_rationale_need_location));
        } else callback.invoke(origin, true, false);
    }

    void openFileChooserBeforeLolipop(ValueCallback<Uri> uploadMessage) {
        uploadMessageBeforeLolipop = uploadMessage;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionCheckerHelper != null) {
            permissionCheckerHelper.onRequestPermissionsResult(getContext(), requestCode, permissions, grantResults);
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            Activity activityInstance = getActivity();
            if (activityInstance instanceof BaseSimpleWebViewActivity) {
                BaseSimpleWebViewActivity activity = (BaseSimpleWebViewActivity) activityInstance;
                String activityTitle = activity.getWebViewTitle();
                if (TextUtils.isEmpty(activityTitle) || activityTitle.equals(DEFAULT_TITLE)) {
                    if (activity.getShowTitleBar()) {
                        if (TextUtils.isEmpty(title)) {
                            title = DEFAULT_TITLE;
                        }
                        activity.setWebViewTitle(title);
                        activity.updateTitle(title);
                    }
                }
                if (url.startsWith(BaseWebViewFragment.this.url)) {
                    activity.setOnWebViewPageFinished();
                } else {
                    activity.enableBackButton();
                }
            } else if (activityInstance != null && !activityInstance.isFinishing() && activityInstance instanceof BaseSimpleActivity) {
                ActionBar actionBar = ((AppCompatActivity) activityInstance).getSupportActionBar();
                if (actionBar != null) {
                    if (isHelpUrl(url) && !title.isEmpty()) {
                        actionBar.setTitle(title);
                    } else {
                        String activityExtraTitle = getExtraTitle(activityInstance);
                        if (!TextUtils.isEmpty(activityExtraTitle)) {
                            actionBar.setTitle(activityExtraTitle);
                        } else {
                            actionBar.setTitle(activityInstance.getString(R.string.tokopedia));
                        }
                    }
                }
            }
        }

        private String getExtraTitle(Context context) {
            if (context != null && isAdded()) {
                Activity activity = (Activity) context;
                return activity.getIntent().getStringExtra(ConstantKt.KEY_TITLE);
            } else return "";
        }

        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            webViewClientShouldInterceptRequest(view, request);
            return super.shouldInterceptRequest(view, request);
        }

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

        boolean hasCheckOverrideAtInitialization(String requestUrl) {
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
            String webUrl = view.getUrl();
            onWebPageReceivedError(failingUrl, errorCode, description, webUrl);
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            String webUrl = view.getUrl();
            onWebPageReceivedError(request.getUrl().toString(), error.getErrorCode(),
                    error.getDescription().toString(), webUrl);
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            String webUrl = view.getUrl();
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", request.getUrl().toString());
            messageMap.put("status_code", String.valueOf(errorResponse.getStatusCode()));
            messageMap.put("reason", errorResponse.getReasonPhrase());
            messageMap.put("web_url", webUrl);
            ServerLogger.log(Priority.P1, "WEBVIEW_ERROR_RESPONSE", messageMap);
        }
    }

    private void onWebPageReceivedError(String failingUrl, int errorCode, String description, String webUrl) {
        progressBar.setVisibility(View.GONE);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", failingUrl);
        messageMap.put("error_code", String.valueOf(errorCode));
        messageMap.put("desc", description);
        messageMap.put("web_url", webUrl);
        ServerLogger.log(Priority.P1, "WEBVIEW_ERROR", messageMap);
        if (errorCode == WebViewClient.ERROR_HOST_LOOKUP &&
                description.contains(ERR_INTERNET_DISCONNECTED) &&
                globalError != null && swipeRefreshLayout != null) {
            webView.clearView();
            globalError.setActionClickListener(new Function1<View, Unit>() {
                @Override
                public Unit invoke(View view) {
                    reloadPage();
                    return Unit.INSTANCE;
                }
            });
            globalError.setVisibility(View.VISIBLE);
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setVisibility(View.GONE);
            }
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
            routeToNativeBrowser(webUrl);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private boolean isHelpUrl(String url) {
        return url.contains(HELP_URL);
    }

    // to be overridden
    protected void webViewClientShouldInterceptRequest(WebView view, WebResourceRequest request) {
        //noop
    }

    protected boolean shouldOverrideUrlLoading(@Nullable WebView webview, @NonNull String url) {
        Activity activity = getActivity();
        if (activity == null) {
            return false;
        }
        if ("".equals(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (uri.isOpaque()) {
            return false;
        }
        if (goToLoginGoogle(uri)) return true;
        if (uri.getHost() == null) {
            return false;
        }
        if (isBriIntent(uri)) {
            return handlingBriIntent(url);
        }

        String queryParam = null;
        String headerText = null;

        try {
            queryParam = uri.getQueryParameter(CUST_OVERLAY_URL);
            headerText = uri.getQueryParameter(CUST_HEADER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (url.endsWith(".pdf") && url.startsWith("http")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(decode(uri.toString().replace(GOOGLE_DOCS_PDF_URL, "")))
                    , "application/pdf");
            try {
                getContext().startActivity(intent);
                return true;
            } catch (Exception e) {
                //user does not have a pdf viewer installed
                return loadGoogleDocsUrl(uri);
            }
        }

        if (url.contains(HCI_CAMERA_KTP)) {
            mJsHciCallbackFuncName = uri.getLastPathSegment();
            routeToHomeCredit(ApplinkConst.HOME_CREDIT_KTP_WITH_TYPE, queryParam, headerText);
            return true;
        } else if (url.contains(HCI_CAMERA_SELFIE)) {
            mJsHciCallbackFuncName = uri.getLastPathSegment();
            routeToHomeCredit(ApplinkConst.HOME_CREDIT_SELFIE_WITHOUT_TYPE, queryParam, headerText);
            return true;
        } else if (PARAM_WEBVIEW_BACK.equalsIgnoreCase(url)
                && getActivity() != null) {
            if (getActivity().isTaskRoot()) {
                RouteManager.route(getContext(), ApplinkConst.HOME);
            } else {
                getActivity().finish();
            }
            return true;
        } else if (PLAY_GOOGLE_URL.equalsIgnoreCase(uri.getHost())) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if ((BRANCH_IO_HOST.equalsIgnoreCase(uri.getHost()) || isFDLHostEnabled(uri))
                && !GlobalConfig.isSellerApp()) {
            //Avoid crash in app that doesn't support branch IO
            try {
                Intent intent = RouteManager.getIntentNoFallback(getActivity(), url);
                if (intent != null) {
                    startActivity(intent);
                }
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (SCHEME_INTENT.equalsIgnoreCase(uri.getScheme())) {
            try {
                Intent newIntent = RouteManager.getIntent(getActivity(), url);
                startActivity(newIntent);
            } catch (ActivityNotFoundException e) {
                Timber.w(e);
            }
        }

        if (url.contains(PARAM_EXTERNAL_TRUE)) {
            try {
                routeToNativeBrowser(url);
                hasMoveToNativePage = true;
                return true;
            } catch (ActivityNotFoundException e) {
                return false;
            }
        }
        if (url.contains(LOGIN_APPLINK) || url.contains(REGISTER_APPLINK)) {
            boolean isCanClearCache = remoteConfig.getBoolean(KEY_CLEAR_CACHE, false);
            if (isCanClearCache && url.contains(CLEAR_CACHE_PREFIX)) {
                Intent intent = RouteManager.getIntent(getActivity(), ApplinkConstInternalUserPlatform.LOGOUT);
                intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_RETURN_HOME, false);
                intent.putExtra(ApplinkConstInternalUserPlatform.PARAM_IS_CLEAR_DATA_ONLY, true);
                startActivityForResult(intent, REQUEST_CODE_LOGOUT);
            } else {
                startActivityForResult(RouteManager.getIntent(getActivity(), url), REQUEST_CODE_LOGIN);
            }
            return true;
        }

        if (url.contains(RESOLUTION_APPLINK)) {
            RouteManager.route(getContext(), url);
            activity.finish();
            return true;
        }

        if (isExternalAppLink(url)) {
            return redirectToExternalAppAndFinish(activity, uri);
        }
        if (url.startsWith(ApplinkConst.KYC_FORM_ONLY_NO_PARAM)) {
            gotoAlaCarteKyc(uri);
            return true;
        }

        boolean isNotNetworkUrl = !URLUtil.isNetworkUrl(url);
        if (isNotNetworkUrl) {
            Intent intent = RouteManager.getIntentNoFallback(getActivity(), url);
            if (intent != null) {
                try {
                    hasMoveToNativePage = true;
                    startActivity(intent);
                    finishActivityIfBackPressedDisabled(hasMoveToNativePage);
                } catch (Exception ignored) {
                }
                return true;
            } else {
                logApplinkErrorOpen(url);
            }
        } else { // network url
            if (handleWebUrlLogin(uri)) {
                return true;
            }
        }
        if (!allowOverride) {
            return false;
        }
        hasMoveToNativePage = RouteManagerKt.moveToNativePageFromWebView(getActivity(), url);
        finishActivityIfBackPressedDisabled(hasMoveToNativePage);
        return hasMoveToNativePage;
    }

    private boolean handleWebUrlLogin(Uri uri) {
        if (WWW_TOKOPEDIA_COM.equals(uri.getHost())) {
            List<String> pathSegments = uri.getPathSegments();
            // https://www.tokopedia.com/login/?...
            if (pathSegments.size() == 1 &&
                    LOGIN.equals(pathSegments.get(0)) &&
                    !userSession.isLoggedIn()) {
                startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
                return true;
            }
        }
        return false;
    }

    private boolean isBriIntent(Uri uri) {
        return uri.getScheme().equals("intent") &&
                uri.getHost().contains("kartukreditbri.co.id") &&
                uri.toString().contains("browser_fallback_url=");
    }

    private boolean handlingBriIntent(String briUrl) {
        String newUrl = briUrl.substring(briUrl.indexOf("browser_fallback_url=") + 21, briUrl.length() - 1);
        newUrl = newUrl.substring(0, newUrl.indexOf(";"));
        return launchWebviewForNewUrl(UrlEncoderExtKt.decode(newUrl));
    }

    private boolean launchWebviewForNewUrl(String newUrl) {
        if (webView != null) {
            if (Uri.parse(newUrl).getHost().contains(TOKOPEDIA_STRING)) {
                webView.loadAuthUrl(newUrl, userSession);
            } else {
                webView.loadUrl(newUrl);
            }
            return true;
        } else {
            // change first url directly
            url = newUrl;
            return false;
        }
    }

    /**
     * return true of webview success load.
     */
    private boolean loadGoogleDocsUrl(Uri uri) {
        String googleDocsUrl;
        if (uri.toString().startsWith(GOOGLE_DOCS_PDF_URL)) {
            googleDocsUrl = decode(uri.toString());
        } else {
            googleDocsUrl = GOOGLE_DOCS_PDF_URL + decode(uri.toString());
        }
        if (webView != null) {
            if (uri.getHost().contains(TOKOPEDIA_STRING)) {
                webView.loadAuthUrl(googleDocsUrl, userSession);
            } else {
                webView.loadUrl(googleDocsUrl);
            }
            return true;
        } else {
            // change first url directly
            url = googleDocsUrl;
            return false;
        }
    }

    private void gotoAlaCarteKyc(Uri uri) {
        String projectId = uri.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_PROJECT_ID);
        String kycRedirectionUrl = uri.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_REDIRECT_URL);
        String layout = uri.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_SHOW_INTRO);
        String kycType = uri.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_KYC_TYPE);
        Intent intent = RouteManager.getIntent(getActivity(), ApplinkConst.KYC_FORM_ONLY, projectId, layout, kycRedirectionUrl, kycType);
        startActivityForResult(intent, REQUEST_CODE_LIVENESS);
    }

    private boolean isFDLHostEnabled(Uri uri) {
        if (remoteConfig != null) {
            return remoteConfig.getBoolean(ENABLE_FDL_HOST_WEBVIEW, true)
                    && FDL_HOST.equalsIgnoreCase(uri.getHost());
        } else {
            return FDL_HOST.equalsIgnoreCase(uri.getHost());
        }
    }

    private void routeToHomeCredit(String appLink, String overlayUrl, String headerText) {
        Intent intent = RouteManager.getIntent(getActivity(), appLink);
        if (overlayUrl != null)
            intent.putExtra(CUST_OVERLAY_URL, overlayUrl);
        if (headerText != null)
            intent.putExtra(CUST_HEADER, headerText);
        startActivityForResult(intent, HCI_CAMERA_REQUEST_CODE);
    }

    private boolean redirectToExternalAppAndFinish(Activity activity, Uri uri) {
        Intent intent = WebViewHelper.actionViewIntent(activity, uri);
        if (intent != null) {
            startActivity(intent);
            activity.finish();
            return true;
        } else {
            return false;
        }
    }

    private void routeToNativeBrowser(String browserUrl) {
        RouteManager.route(getContext(), ApplinkConst.BROWSER + "?url=" + browserUrl);
    }

    private void logApplinkErrorOpen(String url) {
        String referrer = getPreviousUri();
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "Webview");
        messageMap.put("source", getClass().getSimpleName());
        messageMap.put("referrer", referrer);
        messageMap.put("uri", url);
        messageMap.put("journey", "-");
        ServerLogger.log(Priority.P1, "APPLINK_OPEN_ERROR", messageMap);
    }

    private void checkActivityFinish() {
        if (hasMoveToNativePage && !webViewHasContent && getActivity() != null) {
            getActivity().finish();
        }
    }

    private boolean goToLoginGoogle(@NonNull Uri uri) {
        String loginType;
        try {
            loginType = uri.getQueryParameter("login_type");
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
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private String getPreviousUri() {
        if (webView == null) {
            return "";
        }
        WebBackForwardList webBackForwardList = webView.copyBackForwardList();
        if (webBackForwardList == null || webBackForwardList.getCurrentIndex() <= 0) {
            return "";
        }
        WebHistoryItem webHistoryItem = webBackForwardList.getItemAtIndex(webBackForwardList.getCurrentIndex() - 1);
        if (webHistoryItem == null) {
            return "";
        }
        return webHistoryItem.getUrl();
    }

    private boolean isExternalAppLink(String url) {
        return url.contains(LINK_AJA_APP_LINK) || url.contains(GOJEK_APP_LINK);
    }

    // If back pressed is disabled and the webview has moved to a native page,
    // finish the webview activity to avoid user not being able to get out of the webview.
    private void finishActivityIfBackPressedDisabled(boolean hasMoveToNativePage) {
        if (hasMoveToNativePage && getActivity() != null
                && getActivity() instanceof BaseSimpleWebViewActivity
                && !((BaseSimpleWebViewActivity) getActivity()).getBackPressedEnabled()) {
            getActivity().finish();
        }
    }

    public TkpdWebView getWebView() {
        return webView;
    }

    public int setWebView() {
        return R.id.webview;
    }

    public int setProgressBar() {
        return R.id.progressbar;
    }

    public void reloadPage() {
        Activity activity = getActivity();
        if (activity instanceof BaseSimpleWebViewActivity) {
            ((BaseSimpleWebViewActivity) activity).setWebViewTitle("");
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        if (globalError != null) {
            globalError.setVisibility(View.GONE);
        }
        webView.reload();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public interface OnLocationRequestListener {
        void onLocationPermissionRequested(GeolocationPermissions.Callback callback, String origin);
    }

}
