package com.tokopedia.tkpd.thankyou.view;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.nps.presentation.view.dialog.AdvancedAppRatingDialog;
import com.tokopedia.tkpd.home.fragment.ReactNativeThankYouPageFragment;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;
import com.tokopedia.tkpdreactnative.react.ReactConst;

import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;
import com.tokopedia.tokocash.CacheUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;


public class ReactNativeThankYouPageActivity extends ReactFragmentActivity<ReactNativeThankYouPageFragment> {
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String PLATFORM = "platform";
    private static final String DIGITAL = "digital";

    private ReactInstanceManager reactInstanceManager;

    @DeepLink("tokopedia://thankyou/{platform}/{template}")
    public static Intent getThankYouPageApplinkIntent(Context context, Bundle bundle) {
        return ReactNativeThankYouPageActivity.createReactNativeActivity(
                context, ReactConst.Screen.THANK_YOU_PAGE,
                "Thank You"
        ).putExtras(bundle);
    }

    public static Intent createReactNativeActivity(Context context,
                                                   String reactScreenName,
                                                   String pageTitle) {
        Intent intent = new Intent(context, ReactNativeThankYouPageActivity.class);
        Bundle extras = new Bundle();
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactInstanceManager = ((ReactApplication) getApplication())
                .getReactNativeHost().getReactInstanceManager();
        PurchaseNotifier.notify(this, getIntent().getExtras());
        resetWalletCache();
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected ReactNativeThankYouPageFragment getReactNativeFragment() {
        Bundle initialProps = getIntent().getExtras();
        initialProps.remove("android.intent.extra.REFERRER");
        initialProps.remove("is_deep_link_flag");
        initialProps.remove("deep_link_uri");
        sendAnalytics(initialProps);
        return ReactNativeThankYouPageFragment.createInstance(initialProps);
    }

    @Override
    protected String getToolbarTitle() {
        return "Tokopedia";
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_OFFICIAL_STORE_REACT;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        reactInstanceManager = ((ReactApplication) getApplication())
                .getReactNativeHost().getReactInstanceManager();
        if (keyCode == KeyEvent.KEYCODE_MENU && reactInstanceManager != null) {
            reactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void sendAnalytics(Bundle initialProps) {
        ThanksTrackerData data = new ThanksTrackerData();
        data.setPlatform(initialProps.getString(ThanksTrackerConst.Key.PLATFORM));
        data.setTemplate(initialProps.getString(ThanksTrackerConst.Key.TEMPLATE));
        data.setId(initialProps.getString(ThanksTrackerConst.Key.ID));
        if (initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES) != null &&
                !initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES).isEmpty()){
            try {
                data.setShopTypes(Arrays.asList(URLDecoder.decode(initialProps.getString(ThanksTrackerConst.Key.SHOP_TYPES), "UTF-8").split(",")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ThanksTrackerService.start(this, data);
    }

    @Override
    public void onBackPressed() {
        if (isDigital()) {
            AdvancedAppRatingDialog.show(this, dialog -> closeThankyouPage());
        } else {
            closeThankyouPage();
        }
    }

    private boolean isDigital() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String platform = extra.getString(PLATFORM);
            if (platform != null && platform.equals(DIGITAL)) {
                return true;
            }
        }
        return false;
    }

    private void resetWalletCache() {
        if (getApplicationContext() != null && getApplicationContext() instanceof AbstractionRouter) {
            ((AbstractionRouter) getApplicationContext()).getGlobalCacheManager().delete(CacheUtil.KEY_TOKOCASH_BALANCE_CACHE);
        }
    }

    private void closeThankyouPage() {
        super.onBackPressed();
    }
}
