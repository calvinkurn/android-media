package com.tokopedia.core.home;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.fragment.FragmentShopPreview;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.fragment.FragmentBannerWebView;
import com.tokopedia.core.home.fragment.SimpleWebViewWithFilePickerFragment;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.DeepLinkChecker;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;

/**
 * Created by Nisie on 22/10/15.
 */
public class BannerWebView extends TkpdCoreWebViewActivity implements
        FragmentGeneralWebView.OnFragmentInteractionListener, DeepLinkWebViewHandleListener {

    private static final String FLAG_APP = "?flag_app=1";
    private static final java.lang.String ARGS_PROMO_ID = "promo_id";

    public static final String EXTRA_URL = "url";

    private GeneralWebView generalWebView;

    @DeepLink({Constants.Applinks.PROMO})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        String promoId = bundle.getString(ARGS_PROMO_ID, "");
        String result = TkpdBaseURL.URL_PROMO;
        if (!TextUtils.isEmpty(promoId)) {
            result += promoId;
        }
        result += FLAG_APP;
        bundle.putString(BannerWebView.EXTRA_URL, result);
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, BannerWebView.class)
                .setData(uri.build())
                .putExtra(BannerWebView.EXTRA_URL, result);
    }

    public static Intent getCallingIntent(Activity activity, String url) {
        Intent intent = new Intent(activity, BannerWebView.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    public static Intent getCallingIntentWithTitle(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, BannerWebView.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
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
        String url = getIntent().getExtras().getString(EXTRA_URL);
        generalWebView = FragmentBannerWebView.createInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, (FragmentBannerWebView) generalWebView);
            fragmentTransaction.commit();
        }

    }


    public void openShop(String url) {
        Fragment fragment = FragmentShopPreview.createInstanceForDeeplink(DeepLinkChecker.getLinkSegment(url).get(0), url);
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
        generalWebView = SimpleWebViewWithFilePickerFragment.createInstance(url);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, (SimpleWebViewWithFilePickerFragment) generalWebView).commit();
    }

    @Override
    public void onBackPressed() {
        try {
            if (generalWebView.getWebview().canGoBack()) {
                generalWebView.getWebview().goBack();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
