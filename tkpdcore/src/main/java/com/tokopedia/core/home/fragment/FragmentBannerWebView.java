package com.tokopedia.core.home.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.home.GeneralWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.TkpdWebView;

import retrofit2.http.Url;

/**
 * Created by Nisie on 8/25/2015.
 */
public class FragmentBannerWebView extends Fragment implements GeneralWebView {

    private static final String SEAMLESS = "seamless";
    private static final String TOKOPEDIA_LINK = "tokopedia.com";
    private static final String WWW = "www";
    private ProgressBar progressBar;
    private TkpdWebView webview;
    private static final String EXTRA_URL = "url";
    private static final String EXTRA_OVERRIDE_URL = "override_url";
    private static final String LOGIN_TYPE = "login_type";
    private static final String QUERY_PARAM_PLUS = "plus";
    private static final int LOGIN_GPLUS = 123453;
    private boolean isAlreadyFirstRedirect;

    private ValueCallback<Uri> callbackBeforeL;
    public ValueCallback<Uri[]> callbackAfterL;
    public final static int ATTACH_FILE_REQUEST = 1;

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == 100) {
                    view.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    getActivity().setProgressBarIndeterminateVisibility(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onProgressChanged(view, newProgress);
        }

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
    }

    private class MyWebClient extends WebViewClient {
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return overrideUrl(url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
            progressBar.setVisibility(View.GONE);
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CommonUtils.dumper("DEEPLINK " + errorCode + "  " + description + " " + failingUrl);
            super.onReceivedError(view, errorCode, description, failingUrl);
            progressBar.setVisibility(View.GONE);
        }

    }

    public FragmentBannerWebView() {
    }

    public static FragmentBannerWebView createInstance(String url) {
        FragmentBannerWebView fragment = new FragmentBannerWebView();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(url)) {
            args.putString(EXTRA_URL, url);
            Uri uri = Uri.parse(url);
            if (uri.getQueryParameter(EXTRA_OVERRIDE_URL) != null) {
                args.putBoolean(EXTRA_OVERRIDE_URL, uri.getQueryParameter(EXTRA_OVERRIDE_URL).equalsIgnoreCase("1"));
            }
        }
        fragment.setArguments(args);
        return fragment;
    }

    private boolean overrideUrl(String url) {
        if (getActivity() != null && getActivity().getApplication() != null) {
            if (getActivity().getApplication() instanceof IDigitalModuleRouter && (((IDigitalModuleRouter) getActivity().getApplication())
                    .isSupportedDelegateDeepLink(url))) {
                ((IDigitalModuleRouter) getActivity().getApplication())
                        .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                return true;
            } else if (Uri.parse(url).getScheme().equalsIgnoreCase(Constants.APPLINK_CUSTOMER_SCHEME)) {
                if (getActivity().getApplication() instanceof TkpdCoreRouter &&
                        (((TkpdCoreRouter) getActivity().getApplication()).getApplinkUnsupported(getActivity()) != null)) {

                    ((TkpdCoreRouter) getActivity().getApplication())
                            .getApplinkUnsupported(getActivity())
                            .showAndCheckApplinkUnsupported();
                    return true;
                }
            }
        }

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(Uri.parse(url).getHost())) {
            return false;
        }
        if (((Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.WEB_DOMAIN).getHost()))
                || Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.MOBILE_DOMAIN).getHost()))
                && !(Uri.parse(url).getLastPathSegment() != null && Uri.parse(url).getLastPathSegment().endsWith(".pl"))
                && !url.contains("login")) {

            String query = Uri.parse(url).getQueryParameter(LOGIN_TYPE);
            if (query != null && query.equals(QUERY_PARAM_PLUS)) {
                Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext())
                        .getLoginGoogleIntent(getActivity());
                startActivityForResult(intent, LOGIN_GPLUS);
                return true;
            }

            switch ((DeepLinkChecker.getDeepLinkType(url))) {
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
                case DeepLinkChecker.SHOP:
                    ((BannerWebView) getActivity()).openShop(url);
                    return true;
                case DeepLinkChecker.HOME:
                    DeepLinkChecker.openHomepage(getActivity(), HomeRouter.INIT_STATE_FRAGMENT_HOME);
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_GPLUS) {
            String historyUrl = "";
            WebBackForwardList mWebBackForwardList = webview.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0)
                historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
            if (!historyUrl.contains(SEAMLESS))
                webview.loadAuthUrl(URLGenerator.generateURLSessionLogin(historyUrl, getActivity()));
            else {
                webview.loadAuthUrl(historyUrl);
            }
        }
    }

    @Override
    public void onDestroyView() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        String url = getArguments().getString(EXTRA_URL, "http://blog.tokopedia.com");
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        Uri uri = Uri.parse(url);
        if (uri.getHost() != null && !uri.getHost().contains(TOKOPEDIA_LINK)) {
            webview.loadOtherUrl(url);
        } else if (!url.contains(SEAMLESS))
            webview.loadAuthUrl(URLGenerator.generateURLSessionLogin(url, getActivity()));
        else {
            webview.loadAuthUrl(url);
        }
        webview.setWebViewClient(new MyWebClient());
        webview.setWebChromeClient(new MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            webview.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        }
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        optimizeWebView();
        CookieManager.getInstance().setAcceptCookie(true);
        return view;
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    public WebView getWebview() {
        return webview;
    }

    public void setWebview(TkpdWebView webview) {
        this.webview = webview;
    }

    private void optimizeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

}
