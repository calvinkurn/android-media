package com.tokopedia.loginregister.loginthirdparty.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.loginregister.R;
import com.tokopedia.sessioncommon.ErrorHandlerSession;

import java.util.Set;

/**
 * Created by stevenfredian on 5/31/16.
 */
public class WebViewLoginFragment extends DialogFragment {
    public static final String NAME = "NAME";
    //private View pic;

    String url;
    private String name;


    public static WebViewLoginFragment createInstance(String url, String name) {
        WebViewLoginFragment fragment = new WebViewLoginFragment();
        fragment.url = url;
        fragment.name = name;
        return fragment;
    }

    public WebViewLoginFragment() {
    }

    private TkpdWebView webViewOauth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_webview_login, container);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webViewOauth = view.findViewById(R.id.web_oauth);
        progressBar = view.findViewById(R.id.progressBar);
        CookieSyncManager.createInstance(getActivity());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        webViewOauth.clearCache(true);
        webViewOauth.loadUrlWithFlags(url);
        webViewOauth.setWebViewClient(new AuthWebClient());
        webViewOauth.clearHistory();
        webViewOauth.clearFormData();
        webViewOauth.requestFocus(View.FOCUS_DOWN);
        WebSettings webSettings = webViewOauth.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

    private class AuthWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //check if the login was successful and the access token returned
            //this test depend of your API
            return parseUrl(url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (isAdded()) {
                Bundle bundle = new Bundle();
                bundle.putString("error", String.valueOf(error));
                Intent intent = new Intent();
                intent.putExtra("bundle", bundle);
                dismissAllowingStateLoss();
                if (getActivity() != null)
                    NetworkErrorHelper.showSnackbar(getActivity(), ErrorHandlerSession
                            .getDefaultErrorCodeMessage(ErrorHandlerSession.ErrorCode
                                    .WEBVIEW_ERROR, getActivity()));
            }
        }
    }

    private boolean parseUrl(String url) {
        Uri uri = Uri.parse(url);
        String server = uri.getAuthority();
        String path = uri.getPath();
        Set<String> args = uri.getQueryParameterNames();
        if (server.startsWith("accounts") && server.endsWith("tokopedia.com") &&
                (path.contains("code") || path.contains("error") || path.contains("activation-social"))) {
            Bundle bundle = new Bundle();
            bundle.putString("server", server);
            bundle.putString("path", path);
            bundle.putString(NAME, name);
            for (String arg : args) {
                String limit = uri.getQueryParameter(arg);
                bundle.putString(arg, limit);
            }
            Intent intent = new Intent();
            intent.putExtra("bundle", bundle);
            if (getTargetFragment() != null) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        if (getDialog().getWindow() != null) {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
            super.onResume();
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    if (webViewOauth.canGoBack()) {
                        webViewOauth.goBack();
                        return true;
                    } else {
                        dismiss();
                        return false;
                    }
                }
                return false;
            });
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (getTargetFragment() != null &&
                getTargetFragment().isVisible()
                && getActivity() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
        }
        super.onDismiss(dialog);
    }
}
