package com.tokopedia.tkpd.tkpdcontactus.home.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

/**
 * Created by sandeepgoyal on 06/04/18.
 */

public class ContactUsWebViewActivity extends BaseSimpleActivity {
    public static final String KEY_URL = "key_url";
    String url = "";

    public static Intent getInstance(Context context, String url) {
        Intent intent = new Intent(context,ContactUsWebViewActivity.class);
        intent.putExtra(KEY_URL,url);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {

        return BaseSessionWebViewFragment.newInstance(getContactUsArticleUrl());
    }

    private String getContactUsArticleUrl() {
        return getIntent().getStringExtra(KEY_URL);
    }
}
