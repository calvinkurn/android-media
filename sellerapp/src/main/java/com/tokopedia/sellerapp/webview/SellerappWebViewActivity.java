package com.tokopedia.sellerapp.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

public class SellerappWebViewActivity extends BaseSimpleActivity {

    public static final String PARAM_BUNDLE_URL = "bundle_url";
    private String url;


    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, SellerappWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_BUNDLE_URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(PARAM_BUNDLE_URL);
        }
        return SellerappWebViewFragment.newInstance(url);
    }
}
