package com.tokopedia.sellerapp.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user.session.UserSession;

import static com.tokopedia.sellerapp.webview.SellerappWebViewActivity.PARAM_BUNDLE_URL;

public class SellerappWebViewFragment extends BaseWebViewFragment {


    private String url = "";

    public static SellerappWebViewFragment newInstance(String url) {
        SellerappWebViewFragment sellerappWebViewFragment = new SellerappWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUNDLE_URL, url);
        sellerappWebViewFragment.setArguments(bundle);
        return sellerappWebViewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(PARAM_BUNDLE_URL);
        }

    }

    @Override
    protected String getUrl() {
            UserSessionInterface usersession = new UserSession(getActivity());
            return URLGenerator.generateURLSessionLogin(url,
                    usersession.getDeviceId(),
                    usersession.getUserId());


    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        UserSessionInterface usersession = new UserSession(getActivity());
        return usersession.getUserId();
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        UserSessionInterface usersession = new UserSession(getActivity());
        return usersession.getAccessToken();
    }

    @Override
    protected void loadWeb() {
        super.loadWeb();
        webView.getSettings().setBuiltInZoomControls(false);
    }
}
