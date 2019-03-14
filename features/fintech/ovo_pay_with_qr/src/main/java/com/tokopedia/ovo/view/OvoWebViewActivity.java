package com.tokopedia.ovo.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseSessionWebViewFragment;

public class OvoWebViewActivity extends BaseSimpleActivity {

    private static final String URL = "url";

    public static Intent createInstance(Context context, String url) {
        Intent intent = new Intent(context, OvoWebViewActivity.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return BaseSessionWebViewFragment.newInstance(getIntent().getStringExtra(URL));
    }
}
