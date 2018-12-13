package com.tokopedia.core.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.home.fragment.BrandsWebViewFragment;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;

/**
 * Created by brilliant.oka on 15/03/17.
 */

public class BrandsWebViewActivity extends TkpdCoreWebViewActivity implements
        FragmentGeneralWebView.OnFragmentInteractionListener {

    public static final String EXTRA_URL = "url";
    private BrandsWebViewFragment fragment;

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, BrandsWebViewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_webview_container);
        String url = getIntent().getExtras().getString(EXTRA_URL);
        fragment = BrandsWebViewFragment.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment.getWebview().canGoBack()) {
                fragment.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception ex) {
            super.onBackPressed();
        }
    }
}
