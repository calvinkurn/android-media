package com.tokopedia.kol.feature.createpost.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.kol.R;

/**
 * @author by yfsx on 07/06/18.
 */
public class CreatePostWebviewFragment extends BaseWebViewFragment {

    public static final String FORM_URL = "form_url";

    public static CreatePostWebviewFragment newInstance(Bundle bundle) {
        CreatePostWebviewFragment fragment = new CreatePostWebviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected String getUrl() {
        return getArguments().getString(FORM_URL);
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
