package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.webview.BaseSessionWebViewFragment;

public class OvoWebViewActivity extends BaseSimpleActivity {
    private static final String URL = "URL";
    private static final String TITLE = "TITLE";
    public static Intent getWebViewIntent(Context context, String url, String title){
        Intent intent = new Intent(context, OvoWebViewActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        updateTitle(getIntent().getStringExtra(TITLE));
        return BaseSessionWebViewFragment.newInstance(getIntent().getStringExtra(URL));
    }
}
