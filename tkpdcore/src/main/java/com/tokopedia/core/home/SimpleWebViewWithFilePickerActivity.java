package com.tokopedia.core.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.home.fragment.SimpleWebViewWithFilePickerFragment;
import com.tokopedia.core2.R;

public class SimpleWebViewWithFilePickerActivity extends TkpdCoreWebViewActivity {
    public static final String EXTRA_URL = "url";
    private SimpleWebViewWithFilePickerFragment fragment;

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, SimpleWebViewWithFilePickerActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getIntentWithTitle(Context context, String url, String title) {
        Intent intent = new Intent(context, SimpleWebViewWithFilePickerActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        inflateView(R.layout.activity_webview_container);
        String url = getIntent().getExtras().getString(EXTRA_URL);
        fragment = SimpleWebViewWithFilePickerFragment.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.parent_view, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

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

    @Override
    protected int getContentId() {
        return R.layout.activity_webview_container;
    }

}
