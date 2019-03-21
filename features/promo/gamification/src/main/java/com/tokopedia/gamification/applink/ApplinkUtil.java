package com.tokopedia.gamification.applink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;

/**
 * Created by hendry on 06/04/18.
 */

public class ApplinkUtil {

    public static void navigateToAssociatedPage(Activity activity, String applink, String url,
                                                Class<?> defaultClassToNavigate) {

        if (!TextUtils.isEmpty(applink) && ((GamificationRouter) activity.getApplicationContext())
                .isSupportedDelegateDeepLink(applink)) {
            RouteManager.route(activity, applink);
        } else if (!TextUtils.isEmpty(url) && URLUtil.isNetworkUrl(url)) {
            String defaultTitle = activity.getResources().getString(R.string.toko_points_title);
            Intent intent = ((GamificationRouter) activity.getApplicationContext())
                    .getWebviewActivityWithIntent(activity, url, defaultTitle);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, defaultClassToNavigate);
            activity.startActivity(intent);
        }
    }

    public static void navigateToAssociatedPage(Context activity, String applink, String url,
                                                Class<?> defaultClassToNavigate) {

        if (!TextUtils.isEmpty(applink) && ((GamificationRouter) activity.getApplicationContext())
                .isSupportedDelegateDeepLink(applink)) {
            RouteManager.route(activity, applink);
        } else if (!TextUtils.isEmpty(url) && URLUtil.isNetworkUrl(url)) {
            String defaultTitle = activity.getResources().getString(R.string.toko_points_title);
            Intent intent = ((GamificationRouter) activity.getApplicationContext())
                    .getWebviewActivityWithIntent(activity, url, defaultTitle);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, defaultClassToNavigate);
            activity.startActivity(intent);
        }
    }
}
