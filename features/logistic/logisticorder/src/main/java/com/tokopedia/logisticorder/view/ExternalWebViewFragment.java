package com.tokopedia.logisticorder.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tokopedia.logisticorder.R;

public class ExternalWebViewFragment extends Fragment {

    public static final String ARGS_URL = "arg_url";
    private String url;
    private WebView webView;

    public static ExternalWebViewFragment newInstance(String url) {
        ExternalWebViewFragment fragment = new ExternalWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(ARGS_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_external_webview, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        webView = view.findViewById(R.id.webview);
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
    }
}
