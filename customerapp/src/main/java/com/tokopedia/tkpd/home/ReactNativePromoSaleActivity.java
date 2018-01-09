package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ReactNativePromoSaleFragment;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;
import com.tokopedia.tkpdreactnative.react.app.ReactNativeView;

/**
 * Created by yogieputra on 08/01/18.
 */

public class ReactNativePromoSaleActivity extends ReactFragmentActivity<ReactNativePromoSaleFragment> {
    private static final String SALE_PROMO = "Promo Sale";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String KEY_SLUG = "slug";


    @DeepLink({Constants.Applinks.PROMO_SALE})
    public static Intent getPromoSaleApplinkCallingIntent(Context context, Bundle bundle) {
        ScreenTracking.screen(SALE_PROMO);
        return ReactNativePromoSaleActivity.createBannerReactNativeActivity(
                context,
                ReactConst.Screen.PROMO,
                bundle.getString(KEY_SLUG),
                bundle
        );
    }

    @DeepLink({Constants.Applinks.PROMO_SALE_TERMS})
    public static Intent getPromoSaleTermsIntent(Context context, Bundle bundle) {
        return ReactNativePromoSaleActivity.createPromoSaleTerms(
                context,
                ReactConst.Screen.PROMO,
                context.getString(R.string.promo_title_terms),
                bundle
        );
    }

    public static Intent createBannerReactNativeActivity(Context context, String reactScreenName, String url, Bundle extras) {
        Intent intent = new Intent(context, ReactNativePromoSaleActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, "");
        extras.putString(EXTRA_URL, url);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private static Intent createPromoSaleTerms(Context context, String reactScreenName, String title, Bundle extras) {
        Intent intent = new Intent(context, ReactNativePromoSaleActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(ReactConst.SUB_PAGE, ReactConst.Screen.PROMO_TERMS);
        extras.putString(EXTRA_TITLE, title);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected ReactNativePromoSaleFragment getReactNativeFragment() {
        return ReactNativePromoSaleFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }
}
