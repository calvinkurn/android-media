package com.tokopedia.changephonenumber.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;
import com.tokopedia.changephonenumber.view.fragment.OvoWebViewFragment;

/**
 * @author by alvinatin on 09/10/18.
 */

public class OvoWebViewActivity extends BaseWebViewActivity {
    public static final String URL = "WEBVIEW_URL";
    private String url;
    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, OvoWebViewActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString(URL);
        }
        return OvoWebViewFragment.newInstance(url);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }
}
