package com.tokopedia.sessioncommon.view.forbidden.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;

/**
 * Created by nakama on 28/02/18.
 */

public class ServiceFragment extends BaseWebViewFragment {

    public static ServiceFragment createInstance(String url) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ServiceFragment.class.getSimpleName(), url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getUrl() {
        return getArguments().getString(ServiceFragment.class.getSimpleName());
    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        return "";
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        return "";
    }
}
