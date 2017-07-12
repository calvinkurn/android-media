package com.tokopedia.core.home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.fragment.FragmentShopPreview;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.fragment.FragmentBannerWebView;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

/**
 * Created by Nisie on 22/10/15.
 */
public class BannerWebView extends TActivity implements
        FragmentGeneralWebView.OnFragmentInteractionListener, DeepLinkWebViewHandleListener {

    private static final String FLAG_APP = "?flag_app=1";
    private static final java.lang.String ARGS_PROMO_ID = "promo_id";
    private FragmentBannerWebView fragment;
    public static final String EXTRA_URL = "url";

    @DeepLink({Constants.Applinks.PROMO, Constants.Applinks.PROMO_WITH_DASH})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        String promoId = bundle.getString(ARGS_PROMO_ID, "");
        String result = TkpdBaseURL.URL_PROMO;
        if (!TextUtils.isEmpty(promoId)) {
            result += promoId;
        }
        result += FLAG_APP;
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, BannerWebView.class)
                .setData(uri.build())
                .putExtra(BannerWebView.EXTRA_URL, result);
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_WEBVIEW_BANNER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_webview_container);

        String url = getIntent().getExtras().getString(EXTRA_URL);
        fragment = FragmentBannerWebView.createInstance(url);
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
        FragmentBannerWebView fragment = FragmentBannerWebView.createInstance(url);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
