package com.tokopedia.core.router.home;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.util.RouterUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Kulomady on 11/18/16.
 */

public class HomeRouter {

    public static final String EXTRA_BANNERWEBVIEW_URL = "url";
    public static final String TAG_FETCH_BANK = "FETCH_BANK";

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final String EXTRA_APPLINK_UNSUPPORTED = "EXTRA_APPLINK_UNSUPPORTED";
    public static final String EXTRA_APPLINK = "EXTRA_APPLINK";
    public static final String IDENTIFIER_HOME_ACTIVITY = "ParentIndexHome";
    public static final String IDENTIFIER_CATEGORY_FRAGMENT = "FragmentIndexCategory";

    public static final int INIT_STATE_FRAGMENT_HOME = 0;
    public static final int INIT_STATE_FRAGMENT_FEED = 1;
    public static final int INIT_STATE_FRAGMENT_FAVORITE = 2;
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;

    private static final String ACTIVITY_PARENT_INDEX_HOME = "com.tokopedia.tkpd.home.ParentIndexHome";
    private static final String ACTIVITY_BANNER_WEBVIEW = "com.tokopedia.core.home.BannerWebView";
    private static final String FCM_NOTIFICATIONRECEIVER = "com.tokopedia.tkpd.fcm.AppNotificationReceiver";

    @Deprecated
    public static Intent getHomeActivity(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_PARENT_INDEX_HOME);
    }

    public static Intent getHomeActivityInterfaceRouter(Context context) {
        return ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
    }

    public static Intent getBannerWebviewActivity(Context context, String url) {
        Intent intent = RouterUtils.getActivityIntent(context, ACTIVITY_BANNER_WEBVIEW);
        intent.putExtra(EXTRA_BANNERWEBVIEW_URL, url);
        return intent;
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


    public static ComponentName getActivityHomeName(Context context) {
        return RouterUtils.getActivityComponentName(context, ACTIVITY_PARENT_INDEX_HOME);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public static Activity getHomeActivity() {
        Activity activity = null;
        try {
            Class homeClass = RouterUtils.getActivityClass(ACTIVITY_PARENT_INDEX_HOME);
            activity = (Activity) homeClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return activity;
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
