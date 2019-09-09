package com.tokopedia.sellerapp.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.network.constants.TkpdBaseURL;

public class SellerappWebViewActivity extends BaseSimpleActivity {

    public static final String PARAM_BUNDLE_URL = "bundle_url";
    private static final String KEY_APP_LINK_QUERY_URL = "url";
    private String url;

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, SellerappWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUNDLE_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink({ApplinkConst.WEBVIEW, ApplinkConst.SellerApp.WEBVIEW})
    public static Intent createApplinkIntent(Context context, Bundle bundle) {
        return createIntent(context, bundle.getString(KEY_APP_LINK_QUERY_URL, TkpdBaseURL.MOBILE_DOMAIN));
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(PARAM_BUNDLE_URL);
        }
        return SellerappWebViewFragment.newInstance(url);
    }
}
