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

    private static final String FCM_NOTIFICATIONRECEIVER = "com.tokopedia.sellerapp.fcm.AppNotificationReceiver";

    public static Intent getSellerHomeActivity(Context context) {
        return RouterUtils.getRouterFromContext(context).getHomeIntent(context);
    }

    public static Intent getSellerOnBoardingActivity(Context context) {
        return RouterUtils.getRouterFromContext(context).getOnBoardingActivityIntent(context);
    }

    public static Intent getTruecallerIntent(Context context) {
        return RouterUtils.getRouterFromContext(context).getTrueCallerActivityIntent(context);
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