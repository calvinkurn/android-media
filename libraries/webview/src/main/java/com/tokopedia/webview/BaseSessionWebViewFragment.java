package com.tokopedia.webview;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.tokopedia.applink.DeepLinkChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalOrderDetail;
import com.tokopedia.network.utils.URLGenerator;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.webview.download.BaseDownloadAppLinkActivity;

public class BaseSessionWebViewFragment extends BaseWebViewFragment {
    public static final String ARGS_URL = "arg_url";

    private UserSessionInterface userSession;
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
        userSession = new UserSession(getActivity().getApplicationContext());
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

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if(DeepLinkChecker.getDeepLinkType(getContext(),url) == DeepLinkChecker.ORDER_LIST) {
            RouteManager.route(getContext(), ApplinkConstInternalOrderDetail.ORDER_LIST_URL,url);
            return true;
        }
        return super.shouldOverrideUrlLoading(webView, url);
    }

    protected String getPlainUrl() {
        return url;
    }
}
