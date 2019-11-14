package com.tokopedia.saldodetails.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.webview.BaseSessionWebViewFragment;

public class SaldoWebViewActivity extends BaseSimpleActivity {

    private static final String URL = "URL";

    public static Intent getWebViewIntent(Context context, String url) {
        Intent intent = new Intent(context, SaldoWebViewActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(getIntent().getStringExtra(URL));
    }
}
