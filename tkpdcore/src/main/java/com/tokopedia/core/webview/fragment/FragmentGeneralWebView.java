package com.tokopedia.core.webview.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.util.TkpdWebView;


public class FragmentGeneralWebView extends Fragment implements BaseWebViewClient.WebViewCallback, View.OnKeyListener {
    private static final String TAG = FragmentGeneralWebView.class.getSimpleName();

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_OVERRIDE_URL = "allow_override";
    private static final String SEAMLESS = "seamless";

    private TkpdWebView WebViewGeneral;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private String url;

    public static FragmentGeneralWebView createInstance(String url) {
        FragmentGeneralWebView fragment = new FragmentGeneralWebView();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentGeneralWebView createInstance(String url, boolean allowOverride) {
        FragmentGeneralWebView fragment = new FragmentGeneralWebView();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        args.putBoolean(EXTRA_OVERRIDE_URL, allowOverride);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentGeneralWebView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(EXTRA_URL);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CommonUtils.dumper("Load URL: " + url);
        View fragmentView = inflater.inflate(R.layout.fragment_fragment_general_web_view, container, false);
        CookieManager.getInstance().setAcceptCookie(true);
        WebViewGeneral = (TkpdWebView) fragmentView.findViewById(R.id.webview);
        progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressbar);
        progressBar.setIndeterminate(true);
        WebViewGeneral.setOnKeyListener(this);
        if (!url.contains(SEAMLESS))
            WebViewGeneral.loadAuthUrl(URLGenerator.generateURLSessionLogin(url, getActivity()));
        else {
            WebViewGeneral.loadAuthUrl(url);
        }
        WebViewGeneral.getSettings().setJavaScriptEnabled(true);
        WebViewGeneral.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewGeneral.getSettings().setDomStorageEnabled(true);
        if (getArguments().getBoolean(EXTRA_OVERRIDE_URL, false)) {
            WebViewGeneral.setWebViewClient(new MyWebClient());
        } else {
            WebViewGeneral.setWebViewClient(new BaseWebViewClient(this));
        }
        WebViewGeneral.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //  progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        return fragmentView;
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "initial url = " + url);
            try {
                getActivity().setProgressBarIndeterminateVisibility(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "redirect url = " + url);
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

    private boolean overrideUrl(String url) {
        if (((Uri.parse(url).getHost().contains("www.tokopedia.com"))
                || Uri.parse(url).getHost().contains("m.tokopedia.com"))
                && !url.endsWith(".pl")) {
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DetailFragmentInteractionListener");
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
        return false;
    }

    @Override
    public boolean onOverrideUrl(String url) {
        return false;
    }

    public interface OnFragmentInteractionListener {

        void onWebViewSuccessLoad();

        void onWebViewErrorLoad();

        void onWebViewProgressLoad();
    }
}
