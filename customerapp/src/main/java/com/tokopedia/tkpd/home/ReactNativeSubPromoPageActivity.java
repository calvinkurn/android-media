package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.ReactUtils;
import com.tokopedia.tkpdreactnative.react.app.GeneralReactNativeFragment;
import com.tokopedia.tkpdreactnative.react.app.ReactFragmentActivity;

/**
 * Created by yogie putra on 12/04/18.
 */

public class ReactNativeSubPromoPageActivity extends ReactFragmentActivity<GeneralReactNativeFragment> {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String KEY_SLUG = "slug";
    public static final String CATEGORY_SLUG = "category_slug";

    private static final String MP_OS_SUB_PROMO = "mp_os_sub_promo";
    
    @DeepLink({ApplinkConst.SUB_PROMO, ApplinkConst.SUB_PROMO_WITH_SLASH})
    public static Intent getSubPromoApplinkCallingIntent(Context context, Bundle bundle){
        ReactUtils.startTracing(MP_OS_SUB_PROMO);
        return ReactNativeSubPromoPageActivity.createApplinkCallingIntent(
                context,
                ReactConst.Screen.SUB_PROMO,
                "Tokopedia Promo",
                bundle.getString(KEY_SLUG),
                bundle.getString(CATEGORY_SLUG),
                bundle
        );
    }

    public static Intent createApplinkCallingIntent(Context context, String reactScreenName, String pageTitle, String slug, String categorySlug, Bundle extras){
        Intent intent = new Intent(context, ReactNativeSubPromoPageActivity.class);
        extras.putString(ReactConst.KEY_SCREEN, reactScreenName);
        extras.putString(EXTRA_TITLE, pageTitle);
        extras.putString(KEY_SLUG, slug);
        extras.putString(CATEGORY_SLUG, categorySlug);
        intent.putExtras(extras);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected GeneralReactNativeFragment getReactNativeFragment() {
        return GeneralReactNativeFragment.createInstance(getReactNativeProps());
    }

    @Override
    protected String getToolbarTitle() {
        if (getIntent() != null && getIntent().getExtras() != null){
            return getIntent().getExtras().getString(EXTRA_TITLE);
        }

        return "";
    }
}
