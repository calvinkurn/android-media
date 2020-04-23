package com.tokopedia.core.router.home;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.RouterUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Kulomady on 11/18/16.
 */

public class HomeRouter {

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final String IDENTIFIER_HOME_ACTIVITY = "ParentIndexHome";
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;

    private static final String ACTIVITY_PARENT_INDEX_HOME = "com.tokopedia.navigation.presentation.activity.MainParentActivity";
    private static final String FCM_NOTIFICATIONRECEIVER = "com.tokopedia.tkpd.fcm.AppNotificationReceiver";

    @Deprecated
    public static Intent getHomeActivity(Context context) {
        return getHomeActivityInterfaceRouter(context);
    }

    public static Intent getHomeActivityInterfaceRouter(Context context) {
        return ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
    }

    public static Class<?> getHomeActivityClass() {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = RouterUtils.getActivityClass(ACTIVITY_PARENT_INDEX_HOME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
    }

    public static Class<?> getHomeActivityClassInterfaceRouter(Context context) {
        Class<?> parentIndexHomeClass = null;
        try {
            parentIndexHomeClass = ((TkpdCoreRouter) context.getApplicationContext()).getHomeClass(context);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return parentIndexHomeClass;
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
