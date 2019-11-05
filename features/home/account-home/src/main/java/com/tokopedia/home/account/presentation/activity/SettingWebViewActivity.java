package com.tokopedia.home.account.presentation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

public class SettingWebViewActivity extends BaseSimpleActivity {
    private static final String ARG_URL = "url";
    private static final String ARG_TITLE = "title";

    private String url;
    private String title;

    public static Intent createIntent(Context context, String url, String title){
        return new Intent(context, SettingWebViewActivity.class)
                .putExtra(ARG_URL, url)
                .putExtra(ARG_TITLE, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(ARG_URL);
        title = getIntent().getStringExtra(ARG_TITLE);
        super.onCreate(savedInstanceState);
        updateTitle(title);
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(url);
    }
}
