package com.tokopedia.checkout.view.feature.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * Created by Fajar Ulin Nuha on 23/11/18.
 */
public class CheckoutWebViewActivity extends BaseWebViewActivity {

    public static final String EXTRA_URL = "EXTRA_URL";
    private String mUrl;

    public static Intent newInstance(Context context, String url, String title) {
        Intent intent = new Intent(context, CheckoutWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        return BaseSessionWebViewFragment.newInstance(mUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
