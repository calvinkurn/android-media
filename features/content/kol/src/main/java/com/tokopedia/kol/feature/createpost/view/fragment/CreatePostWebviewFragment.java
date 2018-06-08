package com.tokopedia.kol.feature.createpost.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostWebviewFragment extends BaseWebViewFragment {

    private static final String PARAM_URL = "param_url";

    public static CreatePostWebviewFragment newInstance(Bundle bundle) {
        CreatePostWebviewFragment fragment = new CreatePostWebviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String getUrl() {
        return getArguments().getString(PARAM_URL);
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
