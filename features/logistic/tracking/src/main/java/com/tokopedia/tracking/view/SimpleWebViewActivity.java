package com.tokopedia.tracking.view;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseWebViewActivity;

/**
 * @author anggaprasetiyo on 22/05/18.
 */
public class SimpleWebViewActivity extends BaseWebViewActivity {
    public static final String EXTRA_URL = "EXTRA_URL";

    public static Intent createIntent(Context context, String extraUrl) {
        return new Intent(context, SimpleWebViewActivity.class).putExtra(EXTRA_URL, extraUrl);
    }

    @Nullable
    @Override
    protected Intent getContactUsIntent() {
        return null;
    }

    @Override
    protected Fragment getNewFragment() {
        return ExternalWebViewFragment.newInstance(getIntent().getStringExtra(EXTRA_URL));
    }
}
