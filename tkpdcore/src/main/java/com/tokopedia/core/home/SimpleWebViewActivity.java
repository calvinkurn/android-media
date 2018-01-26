package com.tokopedia.core.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.R;
import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.home.fragment.SimpleWebViewFragment;

/**
 * Created by okasurya on 1/23/18.
 * For simple webview, without any override url and such logic, just load a url
 * For another usage please use other webview, currently we have a lot of em
 */

public class SimpleWebViewActivity extends TkpdCoreWebViewActivity {
    public static final String EXTRA_URL = "url";
    private SimpleWebViewFragment fragment;

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, SimpleWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getIntentWithTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, SimpleWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_webview_container);
        String url = getIntent().getExtras().getString(EXTRA_URL);
        fragment = SimpleWebViewFragment.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment.getWebview().canGoBack()) {
                fragment.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
