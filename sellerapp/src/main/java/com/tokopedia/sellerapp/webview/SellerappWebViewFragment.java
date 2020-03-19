package com.tokopedia.sellerapp.webview;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.user.session.UserSession;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.tokopedia.sellerapp.webview.SellerappWebViewActivity.PARAM_BUNDLE_URL;

public class SellerappWebViewFragment extends BaseWebViewFragment {


    private String url = "";
    private boolean isTokopediaUrl;

    private static final String TOKOPEDIA_STRING = "tokopedia";

    public static SellerappWebViewFragment newInstance(String url) {
        SellerappWebViewFragment sellerappWebViewFragment = new SellerappWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUNDLE_URL, getEncodedUrl(url));
        sellerappWebViewFragment.setArguments(bundle);
        return sellerappWebViewFragment;
    }

    private static String getEncodedUrl(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(PARAM_BUNDLE_URL);
        }
        String host = Uri.parse(url).getHost();
        if (host != null && host.contains(TOKOPEDIA_STRING)) {
            isTokopediaUrl = true;
        }
    }

    @Override
    protected String getUrl() {
        if (isTokopediaUrl) {
            UserSessionInterface usersession = new UserSession(getActivity());
            return URLGenerator.generateURLSessionLogin(url,
                    usersession.getDeviceId(),
                    usersession.getUserId());
        } else {
            return url;
        }
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
