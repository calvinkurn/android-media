package com.tokopedia.abstraction.base.view.fragment;

import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;

public class BaseSessionWebViewFragment extends BaseWebViewFragment {
    public static final String ARGS_URL = "arg_url";

    private UserSession userSession;
    private String url;

    public static BaseSessionWebViewFragment newInstance(String url) {
        BaseSessionWebViewFragment fragment = new BaseSessionWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString(ARGS_URL);

        userSession = ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    @Override
    protected String getUrl() {
        String gcmId = userSession.getFcmId();
        String userId = userSession.getUserId();
        return URLGenerator.generateURLSessionLogin(
                Uri.encode(getMitraToppersUrl()),
                gcmId,
                userId);
    }

    @Override
    protected String getUserIdForHeader() {
        return userSession.getUserId();
    }

    private String getMitraToppersUrl() {
        return url;
    }
}
