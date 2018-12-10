package com.tokopedia.loyalty.view.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.ApplinkRouter;
import com.tokopedia.applink.RouteManager;

/**
 * @author okasurya on 1/29/18.
 */

public class TokoPointWebViewFragment extends BaseSessionWebViewFragment {

    public static TokoPointWebViewFragment createInstance(String url) {
        TokoPointWebViewFragment fragment = new TokoPointWebViewFragment();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(url)) {
            args.putString(ARGS_URL, url);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (getActivity() != null && getActivity().getApplication() != null) {
            if (RouteManager.isSupportApplink(getActivity(), url)) {
                RouteManager.route(getActivity(), url);
                return true;
            } else if (Uri.parse(url).getScheme().equalsIgnoreCase(ApplinkConst.APPLINK_CUSTOMER_SCHEME)) {
                if (getActivity().getApplication() instanceof ApplinkRouter &&
                        (((ApplinkRouter) getActivity().getApplication()).getApplinkUnsupported(getActivity()) != null)) {

                    ((ApplinkRouter) getActivity().getApplication())
                            .getApplinkUnsupported(getActivity())
                            .showAndCheckApplinkUnsupported();
                    return true;
                }
            }
        }

        return super.shouldOverrideUrlLoading(webView, url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        clearCache(webView);
        CookieManager.getInstance().setAcceptCookie(true);
        optimizeWebView();
        return view;
    }

    private void clearCache(WebView webView) {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    private void optimizeWebView() {
        webView.setLayerType(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ?
                        View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_SOFTWARE,
                null
        );
    }

    public WebView getWebview() {
        return webView;
    }
}
