package com.tokopedia.core.router;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.util.RouterUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by stevenfredian on 12/1/16.
 */

public class SellerAppRouter {

    private static final String SELLER_HOME_ACTIVITY = "com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity";

    private static final String SELLER_ONBOARDING_ACTIVITY = "com.tokopedia.sellerapp.onboarding.activity.OnboardingSellerActivity";
    private static final String TRUECALLER_ACTIVITY = "com.tokopedia.sellerapp.truecaller.TruecallerActivity";
    private static final String FCM_NOTIFICATIONRECEIVER = "com.tokopedia.sellerapp.fcm.AppNotificationReceiver";


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

    public static IAppNotificationReceiver getAppNotificationReceiver() {
        Constructor<?> ctor = null;
        try {
            ctor = RouterUtils.getActivityClass(FCM_NOTIFICATIONRECEIVER)
                    .getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object object = null;
        try {
            object = ctor.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        return (IAppNotificationReceiver) object;
    }
}