package com.tokopedia.changephonenumber.view.fragment;

import android.os.Bundle;
import android.webkit.URLUtil;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;
import com.tokopedia.applink.RouteManager;

/**
 * @author by alvinatin on 09/10/18.
 */

public class OvoWebViewFragment extends BaseSessionWebViewFragment{

    public static OvoWebViewFragment newInstance(String url) {
        OvoWebViewFragment fragment = new OvoWebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (getContext() != null
                && getContext().getApplicationContext() != null
                && !URLUtil.isNetworkUrl(url)
                && RouteManager.isSupportApplink(getContext(), url)) {
            RouteManager.route(getContext(), url);
            return true;
        }
        return false;
    }
}
