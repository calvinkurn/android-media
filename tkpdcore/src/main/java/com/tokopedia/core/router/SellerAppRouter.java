package com.tokopedia.core.router;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * Created by stevenfredian on 12/1/16.
 */

public class SellerAppRouter {

    private static final String SELLER_HOME_ACTIVITY = "com.tokopedia.sellerapp.home.view.SellerHomeActivity";

    private static final String SELLER_ONBOARDING_ACTIVITY = "com.tokopedia.sellerapp.onboarding.activity.OnboardingSellerActivity";
    private static final String TRUECALLER_ACTIVITY = "com.tokopedia.sellerapp.truecaller.TruecallerActivity";


    public static Intent getSellerHomeActivity(Context context) {
        return RouterUtils.getActivityIntent(context, SELLER_HOME_ACTIVITY);
    }

    public static Intent getSellerOnBoardingActivity(Context context) {
        return RouterUtils.getActivityIntent(context, SELLER_ONBOARDING_ACTIVITY);
    }

    public static Intent getTruecallerIntent(Context context) {
        Intent intent = RouterUtils.getActivityIntent(context, TRUECALLER_ACTIVITY);
        return intent;
    }
}