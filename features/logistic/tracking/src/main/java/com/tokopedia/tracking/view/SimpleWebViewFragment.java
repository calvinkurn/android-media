package com.tokopedia.tracking.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.data.model.session.UserSession;

public class SimpleWebViewFragment extends BaseWebViewFragment {

    public static final String ARGS_URL = "arg_url";
    private String url;

    public static SimpleWebViewFragment newInstance(String url) {
        SimpleWebViewFragment fragment = new SimpleWebViewFragment();
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

    @Nullable
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
