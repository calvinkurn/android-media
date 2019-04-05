package com.tokopedia.abstraction.base.view.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.user.session.UserSession;

/**
 * Please use from module webview instead.
 */
@Deprecated
public class BaseSessionWebViewFragment extends BaseWebViewFragment {
    public static final String ARGS_URL = "arg_url";

    private UserSession userSession;
    private String url;
    public static final String TOKOPEDIA_STRING = "tokopedia";
    private boolean isTokopediaUrl;

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
        isTokopediaUrl = Uri.parse(url).getHost().contains(TOKOPEDIA_STRING);
        userSession = new UserSession(getActivity());
    }

    @Override
    protected String getUrl() {
        if (isTokopediaUrl) {
            String gcmId = userSession.getDeviceId();
            String userId = userSession.getUserId();
            return URLGenerator.generateURLSessionLogin(
                    Uri.encode(getPlainUrl()),
                    gcmId,
                    userId);
        } else {
            return url;
        }
    }

    @Override
    protected String getUserIdForHeader() {
        if (isTokopediaUrl) {
            return userSession.getUserId();
        }
        return null;
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        if (isTokopediaUrl) {
            return userSession.getAccessToken();
        }
        return null;
    }

    protected String getPlainUrl() {
        return url;
    }
}
