package com.tokopedia.sellerapp.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseWebViewFragment;
import com.tokopedia.abstraction.common.utils.network.URLGenerator;

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
        if (getActivity() != null &&
                getActivity().getApplication() instanceof AbstractionRouter) {
            return URLGenerator.generateURLSessionLogin(url,
                    ((AbstractionRouter) getActivity().getApplication()).getSession().getDeviceId(),
                    ((AbstractionRouter) getActivity().getApplication()).getSession().getUserId());
        } else {
            return url;
        }


    }

    @Nullable
    @Override
    protected String getUserIdForHeader() {
        if (getActivity() != null &&
                getActivity().getApplication() instanceof AbstractionRouter) {
            return ((AbstractionRouter) getActivity().getApplication()).getSession().getUserId();
        }
        return "";
    }

    @Nullable
    @Override
    protected String getAccessToken() {
        if (getActivity() != null &&
                getActivity().getApplication() instanceof AbstractionRouter) {
            return ((AbstractionRouter) getActivity().getApplication()).getSession().getAccessToken();
        }
        return "";
    }

    @Override
    protected void loadWeb() {
        super.loadWeb();
        webView.getSettings().setBuiltInZoomControls(false);
    }
}
