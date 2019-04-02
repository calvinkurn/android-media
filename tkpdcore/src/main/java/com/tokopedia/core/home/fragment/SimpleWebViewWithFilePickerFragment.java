package com.tokopedia.core.home.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.home.GeneralWebView;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.TkpdWebView;
import com.tokopedia.core2.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.app.Activity.RESULT_OK;

public class SimpleWebViewWithFilePickerFragment extends Fragment implements GeneralWebView {
    private static final String SEAMLESS = "seamless";
    public static final int PROGRESS_COMPLETED = 100;
    private static WebViewClient webViewClient;
    private ProgressBar progressBar;
    private TkpdWebView webview;
    private ValueCallback<Uri> callbackBeforeL;
    public ValueCallback<Uri[]> callbackAfterL;
    public final static int ATTACH_FILE_REQUEST = 1;
    private static final String EXTRA_URL = "url";

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            try {
                if (newProgress == PROGRESS_COMPLETED) {
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
        private static final String APPLINK_SCHEME = "tokopedia://";

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

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return onOverrideUrl(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return onOverrideUrl(request.getUrl());
        }


        protected boolean onOverrideUrl(Uri url) {
            String urlString = url.toString();
            try {
                //TODO delete this after ws change to new applink
                if (urlString.contains(String.format("%s=true", TkpdInboxRouter.IS_CHAT_BOT))) {
                    String messageId = urlString.toLowerCase().replace("tokopedia://topchat/", "")
                            .replace("?is_chat_bot=true", "");
                    Intent intent = ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .getChatBotIntent(getActivity(), messageId);
                    startActivity(intent);
                    return true;
                } else if (getActivity().getApplicationContext() instanceof TkpdInboxRouter
                        && ((TkpdInboxRouter) getActivity().getApplicationContext()).isSupportedDelegateDeepLink(url.toString())) {
                    ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(), url.toString(), new
                                    Bundle());
                    return true;
                } else if (urlString.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, url);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }

        }


    }

    public static SimpleWebViewWithFilePickerFragment createInstance(String url) {
        return createInstanceWithWebClient(url, null);
    }

    public static SimpleWebViewWithFilePickerFragment createInstanceWithWebClient(String url, WebViewClient client) {
        webViewClient = client;
        SimpleWebViewWithFilePickerFragment fragment = new SimpleWebViewWithFilePickerFragment();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(url)) {
            String encodedUrl;
            try {
                if (url.contains(SEAMLESS))
                    encodedUrl = url;
                else if (Uri.decode(url).equals(url))
                    encodedUrl = URLEncoder.encode(url, "UTF-8");
                else
                    encodedUrl = url;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                encodedUrl = url;
            }
            args.putString(EXTRA_URL, encodedUrl);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == ATTACH_FILE_REQUEST) {
                    if (null == callbackAfterL) {
                        return;
                    }

                    String dataString = intent.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};

                    }
                }
            }
            if (callbackAfterL != null) callbackAfterL.onReceiveValue(results);
            callbackAfterL = null;
        } else {
            if (requestCode == ATTACH_FILE_REQUEST) {
                if (null == callbackBeforeL) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                callbackBeforeL.onReceiveValue(result);
                callbackBeforeL = null;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        String url = getArguments().getString(EXTRA_URL, TkpdBaseURL.MOBILE_DOMAIN);
        webview = (TkpdWebView) view.findViewById(R.id.webview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        clearCache(webview);
        if (!url.contains(SEAMLESS))
            webview.loadAuthUrl(URLGenerator.generateURLSessionLogin(url, getActivity()));
        else {
            webview.loadAuthUrl(url);
        }
        if (webViewClient == null)
            webview.setWebViewClient(new SimpleWebViewWithFilePickerFragment.MyWebClient());
        else
            webview.setWebViewClient(webViewClient);
        webview.setWebChromeClient(new SimpleWebViewWithFilePickerFragment.MyWebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
            CommonUtils.dumper("webviewconf debugging = true");
        }
        getActivity().setProgressBarIndeterminateVisibility(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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

    @Override
    public WebView getWebview() {
        return webview;
    }

    public void setWebview(TkpdWebView webview) {
        this.webview = webview;
    }

    private void optimizeWebView() {
        webview.setLayerType(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                        View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_SOFTWARE,
                null
        );
    }
}