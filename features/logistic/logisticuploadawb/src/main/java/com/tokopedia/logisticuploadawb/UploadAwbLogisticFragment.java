package com.tokopedia.logisticuploadawb;

import android.os.Bundle;
import android.webkit.WebView;

import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * @author anggaprasetiyo on 23/05/18.
 */
public class UploadAwbLogisticFragment extends BaseSessionWebViewFragment {

    public static UploadAwbLogisticFragment newInstance(String url) {
        UploadAwbLogisticFragment fragment = new UploadAwbLogisticFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (getActivity() != null && ((ILogisticUploadAwbRouter) getActivity().getApplication())
                .logisticUploadRouterIsSupportedDelegateDeepLink(url)) {
            ((ILogisticUploadAwbRouter) getActivity().getApplication())
                    .logisticUploadRouterActionNavigateByApplinksUrl(getActivity(), url, new Bundle());
            return true;
        }
        return false;
    }
}
