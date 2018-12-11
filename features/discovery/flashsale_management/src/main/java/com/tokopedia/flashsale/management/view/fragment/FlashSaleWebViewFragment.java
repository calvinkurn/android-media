package com.tokopedia.flashsale.management.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;

public class FlashSaleWebViewFragment extends BaseWebViewFragment {
    public static final String ARGS_URL = "arg_url";
    private String url;

    public static FlashSaleWebViewFragment newInstance(String url) {
        FlashSaleWebViewFragment fragment = new FlashSaleWebViewFragment();
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

    @Override
    protected String getUrl() {
        return url;
    }

    @Override
    protected String getUserIdForHeader() {
        return null;
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        return null;
    }
}
