package com.tokopedia.core.fragment;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdFragment;
import com.tokopedia.core.util.TkpdWebView;

/**
 * Created by hangnadi on 6/5/15.
 */
public class FragmentTermPrivacy extends TkpdFragment {

    public static final int PRIVACY_MODE = 1;
    public static final int TERM_MODE = 0;
    private static int MODE;
    private static String URL;

    private class WebViewHandler extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            holder.loading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            holder.loading.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.cancel();
        }

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private View rootView;
    private ViewHolder holder;
    private WebViewHandler webViewHandler;

    private class ViewHolder {
        TkpdWebView webView;
        View loading;
    }

    public static FragmentTermPrivacy createInstance(int mode) {
        FragmentTermPrivacy fragment = new FragmentTermPrivacy();
        fragment.MODE = mode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MODE==0)
            getActivity().setTitle(getString(R.string.custom_string_term_condition));
        else
            getActivity().setTitle(getString(R.string.custom_string_privacy_policy));
        holder = new ViewHolder();
        webViewHandler = new WebViewHandler();
        setDefaultURL();
    }

    private void setDefaultURL() {
        if(MODE == TERM_MODE) {
            CommonUtils.dumper("dumper masuk term");
            URL = getActivity().getString(R.string.term_pl);
        } else if(MODE == PRIVACY_MODE) {
            CommonUtils.dumper("dumper masuk privacy");
            URL = getActivity().getString(R.string.privacy_pl);
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_term_privacy, container, false);
        initView();
        initData();
        return rootView;
    }

    private void initView() {
        holder.webView = (TkpdWebView) rootView.findViewById(R.id.webview);
        holder.loading = rootView.findViewById(R.id.loading);
    }

    private void initData() {
        holder.webView.loadUrl(URL);
        holder.webView.setWebViewClient(webViewHandler);
    }

}
