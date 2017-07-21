package com.tokopedia.core.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.fragment.FragmentShopPreview;
import com.tokopedia.core.home.fragment.FragmentBannerWebView;
import com.tokopedia.core.home.fragment.FragmentTopPicksWebView;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

/**
 * Created by Alifa on 1/10/2017.
 */

public class TopPicksWebView extends TkpdCoreWebViewActivity implements
        FragmentGeneralWebView.OnFragmentInteractionListener, DeepLinkWebViewHandleListener {

    private static final int IS_WEBVIEW = 1;
    private static final String URL = "url";
    private FragmentTopPicksWebView fragment;

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, TopPicksWebView.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_WEBVIEW_BANNER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_webview_container);

        String url = getIntent().getExtras().getString("url");
        fragment = FragmentTopPicksWebView.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }

    }


    public void openShop(String url) {
        Fragment fragment = FragmentShopPreview.createInstances(DeepLinkChecker.getLinkSegment(url).get(0), url);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
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
    public void catchToWebView(String url) {
        FragmentTopPicksWebView fragment = FragmentTopPicksWebView.createInstance(url);
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
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
