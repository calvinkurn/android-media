package com.tokopedia.tkpd.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativeOfficialStorePromoFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;


/**
 * Created by yogieputra on 05/01/18.
 */

public class ReactNativeOfficialStorePromoActivity extends ReactFragmentActivity<ReactNativeOfficialStorePromoFragment> {

    public static final String OS_PROMO_PAGE = "OS Promo Page";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String KEY_SLUG = "slug";

    private static final String MP_OS_PROMO = "mp_os_promo";

    @DeepLink({Constants.Applinks.OFFICIAL_STORES_PROMO})
    public static Intent getOfficialStoresPromoApplinkCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(context, OS_PROMO_PAGE);
        ReactUtils.startTracing(MP_OS_PROMO);
        return ReactNativeOfficialStorePromoActivity.createBannerReactNativeActivity(
                context,
                ReactConst.Screen.PROMO,
                bundle.getString(KEY_SLUG),
                bundle
        );
    }

    @DeepLink({Constants.Applinks.OFFICIAL_STORE_PROMO})
    public static Intent getOfficialStorePromoApplinkCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(context, OS_PROMO_PAGE);
        ReactUtils.startTracing(MP_OS_PROMO);
        return ReactNativeOfficialStorePromoActivity.createBannerReactNativeActivity(
                context,
                ReactConst.Screen.PROMO,
                bundle.getString(KEY_SLUG),
                bundle
        );
    }

    @DeepLink({Constants.Applinks.OFFICIAL_STORES_PROMO_TERMS})
    public static Intent getOfficialStoreTermsIntent(Context context, Bundle bundle) {
        ReactUtils.startTracing(MP_OS_PROMO);
        return ReactNativeOfficialStorePromoActivity.createOfficialStoreTerms(
                context,
                ReactConst.Screen.PROMO,
                context.getString(R.string.promo_title_terms),
                bundle
        );
    }

    public static Intent createBannerReactNativeActivity(Context context, String reactScreenName, String url, Bundle extras) {
        Intent intent = new Intent(context, ReactNativeOfficialStorePromoActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, "");
        extras.putString(EXTRA_URL, url);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private static Intent createOfficialStoreTerms(Context context, String reactScreenName, String title, Bundle extras) {
        Intent intent = new Intent(context, ReactNativeOfficialStorePromoActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(ReactConst.SUB_PAGE, ReactConst.Screen.PROMO_TERMS);
        extras.putString(EXTRA_TITLE, title);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativeOfficialStorePromoFragment getReactNativeFragment() {
        return ReactNativeOfficialStorePromoFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // No super to avoid crash transactionTooLarge
    }
}

