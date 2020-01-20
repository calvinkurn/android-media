package com.tokopedia.core.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core2.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TkpdCoreWebViewActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.webview.listener.DeepLinkWebViewHandleListener;
import com.tokopedia.webview.BaseSessionWebViewFragment;

import static com.tokopedia.core.network.constants.TkpdBaseURL.FLAG_APP;

/**
 * Created by Alifa on 1/10/2017.
 */

public class TopPicksWebView extends TkpdCoreWebViewActivity implements DeepLinkWebViewHandleListener {

    private static final int IS_WEBVIEW = 1;
    private static final String URL = "url";
    private static final String TOPPICK_SEGMENT = "toppicks";
    private static final String ARGS_TOPPICK_ID = "toppick_id";
    private BaseSessionWebViewFragment fragment;

    public static Intent newInstance(Context context, String url) {
        Intent intent = new Intent(context, TopPicksWebView.class);
        intent.putExtra(URL, url);
        return intent;
    }

    @DeepLink({Constants.Applinks.TOPPICKS, Constants.Applinks.TOPPICK_DETAIL})
    public static Intent getFeedApplinkCallingIntent(Context context, Bundle bundle) {
        String toppickId = bundle.getString(ARGS_TOPPICK_ID, "");
        String result = TkpdBaseURL.WEB_DOMAIN + TOPPICK_SEGMENT;
        if (!TextUtils.isEmpty(toppickId)) {
            result += "/" + toppickId;
        }
        result += FLAG_APP;
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return newInstance(context, result)
                .setData(uri.build())
                .putExtras(bundle);
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
        fragment = BaseSessionWebViewFragment.newInstance(url);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void catchToWebView(String url) {
        BaseSessionWebViewFragment fragment = BaseSessionWebViewFragment.newInstance(url);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        try {
            if (fragment.getWebView().canGoBack()) {
                fragment.getWebView().goBack();
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
