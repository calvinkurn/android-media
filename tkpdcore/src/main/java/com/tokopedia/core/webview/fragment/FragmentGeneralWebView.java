package com.tokopedia.core.webview.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.util.TkpdWebView;


public class FragmentGeneralWebView extends Fragment implements BaseWebViewClient.WebViewCallback, View.OnKeyListener {
    private static final String TAG = FragmentGeneralWebView.class.getSimpleName();

    private TkpdWebView WebViewGeneral;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressBar;
    private String url;

    public static FragmentGeneralWebView createInstance(String url) {
        FragmentGeneralWebView fragment = new FragmentGeneralWebView();
        Bundle args = new Bundle();
        args.putString("url", url);
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
            url = getArguments().getString("url");
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
        WebViewGeneral.loadAuthUrl(URLGenerator.generateURLSessionLogin(url, getActivity()));
        WebViewGeneral.getSettings().setJavaScriptEnabled(true);
        WebViewGeneral.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewGeneral.getSettings().setDomStorageEnabled(true);
        WebViewGeneral.setWebViewClient(new BaseWebViewClient(this));
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


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
