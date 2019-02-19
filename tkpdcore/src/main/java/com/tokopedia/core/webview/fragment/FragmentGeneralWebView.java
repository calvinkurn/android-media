package com.tokopedia.core.webview.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core2.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.net.URLDecoder;

import static android.app.Activity.RESULT_OK;

/**
 * Use webview fragment from tkpd abstraction
 */
@Deprecated
public class FragmentGeneralWebView extends Fragment implements BaseWebViewClient.WebViewCallback,
        View.OnKeyListener {
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_OVERRIDE_URL = "allow_override";
    public static final String EXTRA_SHOW_TOOLBAR = "show_toolbar";
    private static final String TAG = FragmentGeneralWebView.class.getSimpleName();
    private static final String SEAMLESS = "seamless";
    private static final String LOGIN_TYPE = "login_type";
    private static final String QUERY_PARAM_PLUS = "plus";
    private static final String KOL_URL = "tokopedia.com/content";
    private static final String PARAM_WEBVIEW_BACK = "tokopedia://back";
    private static final int LOGIN_GPLUS = 123453;
    private static final int REQUEST_CODE_LOGIN = 123321;
    private static boolean isAlreadyFirstRedirect;
    private TkpdWebView WebViewGeneral;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private String url;
    private boolean showToolbar;
    private ValueCallback<Uri> callbackBeforeL;
    public ValueCallback<Uri[]> callbackAfterL;
    public final static int ATTACH_FILE_REQUEST = 1;
    private boolean doubleTapExit = false;
    private static final int EXIT_DELAY_MILLIS = 2000;

    private boolean pageLoaded = false;

    public FragmentGeneralWebView() {
        // Required empty public constructor
    }

    /**
     * @deprecated Use {@link FragmentGeneralWebView#createInstance(String, boolean, boolean)} ()}
     * instead.
     */
    @Deprecated
    public static FragmentGeneralWebView createInstance(String url) {
        return createInstance(url, false, true);
    }

    public static FragmentGeneralWebView createInstance(String url, boolean allowOverride,
                                                        boolean showToolbar) {
        FragmentGeneralWebView fragment = new FragmentGeneralWebView();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putBoolean(EXTRA_OVERRIDE_URL, allowOverride);
        args.putBoolean(EXTRA_SHOW_TOOLBAR, showToolbar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(EXTRA_URL);
            showToolbar = getArguments().getBoolean(EXTRA_SHOW_TOOLBAR, true);
        }

        if(urlNeedLoginNative(url)){
            startActivityForResult(((TkpdCoreRouter)getActivity().getApplicationContext()).getLoginIntent(getActivity()), REQUEST_CODE_LOGIN);
        }
    }

    private boolean urlNeedLoginNative(String url) {
        UserSessionInterface userSession = new UserSession(getActivity().getApplicationContext());
        boolean urlNeedLogin;
        switch (DeepLinkChecker.getDeepLinkType(url)) {
            case DeepLinkChecker.PLAY: urlNeedLogin = true;
            default : urlNeedLogin = false;
        }

        return !userSession.isLoggedIn() && urlNeedLogin;
    }

    private String decode(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.dumper("Load URL: " + url);
        if (overrideUrl(decode(url))) {
            getActivity().finish();
            return null;
        } else {
            return onCreateWebView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!showToolbar && getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } else if (!showToolbar && getActivity() != null) {
            getActivity().getActionBar().hide();
        }
    }

    private View onCreateWebView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(
                R.layout.fragment_fragment_general_web_view, container, false
        );
        CookieManager.getInstance().setAcceptCookie(true);
        WebViewGeneral = (TkpdWebView) fragmentView.findViewById(R.id.webview);
        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        WebViewGeneral.setOnKeyListener(this);
        WebViewGeneral.loadAuthUrl(!url.contains(SEAMLESS)
                ? URLGenerator.generateURLSessionLogin(url, getActivity()) : url);
        WebViewGeneral.getSettings().setJavaScriptEnabled(true);
        WebViewGeneral.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewGeneral.getSettings().setDomStorageEnabled(true);
        WebViewGeneral.setWebViewClient(new MyWebClient());
        WebViewGeneral.setWebChromeClient(new WebChromeClient() {
            //For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                callbackBeforeL = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
            }

            // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                callbackBeforeL = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"), ATTACH_FILE_REQUEST);
            }

            //For Android 4.1+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                callbackBeforeL = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), ATTACH_FILE_REQUEST);
            }

            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if (callbackAfterL != null) {
                    callbackAfterL.onReceiveValue(null);
                }
                callbackAfterL = filePathCallback;

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
            public void onProgressChanged(WebView view, int newProgress) {
                //  progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (getActivity() instanceof AppCompatActivity
                        && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    String decodedUrl = Uri.decode(url).toLowerCase();

                    if (!TextUtils.isEmpty(title)
                            && Uri.parse(title).getScheme() == null
                            && isKolUrl(decodedUrl)) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                                title
                        );
                    } else {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                                getString(R.string.title_activity_deep_link)
                        );
                    }
                }
            }
        });
        return fragmentView;
    }

    private boolean isKolUrl(String url) {
        return url.contains(KOL_URL);
    }

    public WebView getWebview() {
        return WebViewGeneral;
    }

    private boolean overrideUrl(String url) {
        if (url == null) {
            return false;
        }

        Uri uri = null;
        try {
            uri = Uri.parse(url);
        } catch (Exception ex) {
        }

        if (uri == null || uri.getHost() == null) {
            return false;
        }

        if (((uri.getHost().contains(Uri.parse(TkpdBaseURL.WEB_DOMAIN).getHost()))
                || uri.getHost().contains(Uri.parse(TkpdBaseURL.MOBILE_DOMAIN).getHost()))
                && !url.endsWith(".pl")) {
            CommonUtils.dumper(DeepLinkChecker.getDeepLinkType(url));
            switch (DeepLinkChecker.getDeepLinkType(url)) {
                case DeepLinkChecker.CATEGORY:
                    DeepLinkChecker.openCategory(url, getActivity());
                    return true;
                case DeepLinkChecker.BROWSE:
                    DeepLinkChecker.openBrowse(url, getActivity());
                    return true;
                case DeepLinkChecker.HOT:
                    DeepLinkChecker.openHot(url, getActivity());
                    return true;
                case DeepLinkChecker.CATALOG:
                    DeepLinkChecker.openCatalog(url, getActivity());
                    return true;
                case DeepLinkChecker.PRODUCT:
                    DeepLinkChecker.openProduct(url, getActivity());
                    return true;
                case DeepLinkChecker.HOME:
                    DeepLinkChecker.openHomepage(getActivity(), HomeRouter.INIT_STATE_FRAGMENT_HOME);
                    return true;
                case DeepLinkChecker.TOKOPOINT:
                    DeepLinkChecker.openTokoPoint(getActivity(), url);
                    return true;
                case DeepLinkChecker.WALLET_OVO:
                    return false;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private void openDigitalPage(String applink) {
        if (getActivity().getApplication() instanceof IDigitalModuleRouter) {
            if (((IDigitalModuleRouter) getActivity().getApplication())
                    .isSupportedDelegateDeepLink(applink)) {
                Bundle bundle = new Bundle();
                ((IDigitalModuleRouter) getActivity().getApplication()).actionNavigateByApplinksUrl(getActivity(),
                        applink, bundle);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FragmentGeneralWebView.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccessResult(String successResult) {
        Log.d(TAG, "Success loaded " + successResult);
        if (mListener != null) mListener.onWebViewSuccessLoad();
    }

    @Override
    public void onProgressResult(String progressResult) {
        Log.d(TAG, "Web on load " + progressResult);
        progressBar.setVisibility(View.VISIBLE);
        if (mListener != null) mListener.onWebViewProgressLoad();
    }

    @Override
    public void onErrorResult(SslError errorResult) {
        Log.d(TAG, "Error loaded " + errorResult.toString());
        progressBar.setVisibility(View.GONE);
        if (mListener != null) mListener.onWebViewErrorLoad();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (WebViewGeneral.canGoBack()) {
                        WebViewGeneral.goBack();
                        return true;
                    } else if (!showToolbar) {
                        doubleTapExit();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private void doubleTapExit() {
        if (doubleTapExit) {
            getActivity().finish();
        } else {
            doubleTapExit = true;
            String exitMessage = "Tekan sekali lagi untuk keluar";
            Toast.makeText(getActivity(), exitMessage, Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleTapExit = false, EXIT_DELAY_MILLIS);
        }
    }


    @Override
    public boolean onOverrideUrl(String url) {
        String query = Uri.parse(url).getQueryParameter(LOGIN_TYPE);
        if (query != null && query.equals(QUERY_PARAM_PLUS)) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                    .getLoginGoogleIntent(getActivity());
            startActivityForResult(intent, LOGIN_GPLUS);
            return true;
        }
        return false;
    }

    @Override
    public void onWebTitlePageCompleted(String title) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == callbackAfterL) {
                        return;
                    }

                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};

                    }
                }
            }
            if (callbackAfterL != null) {
                callbackAfterL.onReceiveValue(results);
                callbackAfterL = null;
            }
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == callbackBeforeL) return;
                Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                callbackBeforeL.onReceiveValue(result);
                callbackBeforeL = null;
            }
        }

        if (requestCode == LOGIN_GPLUS || requestCode == REQUEST_CODE_LOGIN) {
            String historyUrl = "";
            WebBackForwardList mWebBackForwardList = WebViewGeneral.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0)
                historyUrl = mWebBackForwardList.getItemAtIndex(
                        mWebBackForwardList.getCurrentIndex() - 1
                ).getUrl();
            if (!historyUrl.contains(SEAMLESS))
                WebViewGeneral.loadAuthUrl(
                        URLGenerator.generateURLSessionLogin(historyUrl, getActivity())
                );
            else {
                WebViewGeneral.loadAuthUrl(historyUrl);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onWebViewSuccessLoad();

        void onWebViewErrorLoad();

        void onWebViewProgressLoad();
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "initial url = " + url);
            try {
                //noinspection deprecation
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "redirect url = " + url);
            if (getActivity() != null
                    && url.equalsIgnoreCase(PARAM_WEBVIEW_BACK)
                    && !getActivity().isTaskRoot()) {
                getActivity().finish();
                return true;
            } else if (getActivity() != null
                    && url.equalsIgnoreCase(PARAM_WEBVIEW_BACK)
                    && !getActivity().isTaskRoot()) {
                openHomePage();
                return true;
            } else if (getActivity() != null && ((IDigitalModuleRouter) getActivity()
                    .getApplication())
                    .isSupportedDelegateDeepLink(url)) {
                if (url.startsWith(Constants.Applinks.WEBVIEW)) {
                    Uri uri = Uri.parse(url);
                    String newUrl = uri.getQueryParameter("url");
                    if (!TextUtils.isEmpty(newUrl)) {
                        newUrl = Uri.decode(newUrl);
                        // Check if url should redirect to applink
                        // Replace old url to new applink
                        if (((IDigitalModuleRouter) getActivity().getApplication()).isSupportedDelegateDeepLink(newUrl)) {
                            url = newUrl;
                        }

                    }
                }
                ((IDigitalModuleRouter) getActivity().getApplication())
                        .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                return true;
            } else if (getActivity() != null &&
                    Uri.parse(url).getScheme().equalsIgnoreCase(Constants.APPLINK_CUSTOMER_SCHEME)) {
                if (getActivity().getApplication() instanceof TkpdCoreRouter &&
                        (((TkpdCoreRouter) getActivity().getApplication()).getApplinkUnsupported(getActivity()) != null)) {

                    ((TkpdCoreRouter) getActivity().getApplication())
                            .getApplinkUnsupported(getActivity())
                            .showAndCheckApplinkUnsupported();
                }
            } else if (getActivity() != null &&
                    getActivity().getApplication() instanceof TkpdCoreRouter) {
                String applink = ((TkpdCoreRouter) getActivity().getApplication())
                        .applink(getActivity(), url);
                if (!TextUtils.isEmpty(applink)) {
                    openDigitalPage(applink);
                    return true;
                }
            }
            return overrideUrl(url);
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

    private void openHomePage() {
        if (getActivity() != null
                && getActivity().getApplicationContext() != null
                && getActivity().getApplicationContext() instanceof TkpdCoreRouter) {
            startActivity(((TkpdCoreRouter) getActivity().getApplicationContext()).getHomeIntent
                    (getActivity()));
            getActivity().finish();
        }
    }

}
