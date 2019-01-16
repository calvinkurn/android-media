package com.tokopedia.loyalty.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.loyalty.view.fragment.TokoPointWebViewFragment;

/**
 * @author okasurya on 1/29/18.
 */

public class TokoPointWebviewActivity extends BaseSimpleActivity {
    public static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";
    private TokoPointWebViewFragment fragment;

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, TokoPointWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getIntentWithTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, TokoPointWebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!TextUtils.isEmpty(getIntent().getStringExtra(EXTRA_TITLE))){
            updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
        }
    }

    @Override
    protected Fragment getNewFragment() {
        fragment = TokoPointWebViewFragment.createInstance(getIntent().getStringExtra(EXTRA_URL));
        return fragment;
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment!= null && fragment.isAdded() && fragment.getWebview().canGoBack()) {
                fragment.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
