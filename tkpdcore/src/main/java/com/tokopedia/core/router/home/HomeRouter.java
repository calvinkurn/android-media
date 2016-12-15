package com.tokopedia.core.router.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.util.RouterUtils;

/**
 * @author Kulomady on 11/18/16.
 */

public class HomeRouter {

    public static final String EXTRA_BANNERWEBVIEW_URL = "url";
    public static final String TAG_FETCH_BANK = "FETCH_BANK";

    public static final String EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT";
    public static final String IDENTIFIER_HOME_ACTIVITY = "ParentIndexHome";
    public static final String IDENTIFIER_CATEGORY_FRAGMENT = "FragmentIndexCategory";

    public static final int INIT_STATE_FRAGMENT_HOME = 0;
    public static final int INIT_STATE_FRAGMENT_FEED = 1;
    public static final int INIT_STATE_FRAGMENT_FAVORITE = 2;
    public static final int INIT_STATE_FRAGMENT_HOTLIST = 3;

    private static final String ACTIVITY_PARENT_INDEX_HOME = "com.tokopedia.tkpd.home.ParentIndexHome";
    private static final String ACTIVITY_BANNER_WEBVIEW = "com.tokopedia.core.home.BannerWebView";


    public static Intent getHomeActivity(Context context) {
        return RouterUtils.getActivityIntent(context, ACTIVITY_PARENT_INDEX_HOME);
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


}
