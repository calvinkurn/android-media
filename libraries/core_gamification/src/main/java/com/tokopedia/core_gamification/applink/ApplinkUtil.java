package com.tokopedia.core_gamification.applink;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.core_gamification.GamificationRouter;
import com.tokopedia.core_gamification.R;

/**
 * Created by hendry on 06/04/18.
 */

public class ApplinkUtil {

    public static void navigateToAssociatedPage(Activity activity, String applink, String url,
                                                Class<?> defaultClassToNavigate) {

        if (!TextUtils.isEmpty(applink) && RouteManager.route(activity, applink)) {
            //Do nothing
        } else if (!TextUtils.isEmpty(url) && URLUtil.isNetworkUrl(url)) {
            String defaultTitle = activity.getResources().getString(R.string.core_gami_toko_points_title);
            Intent intent = ((GamificationRouter) activity.getApplicationContext())
                    .getWebviewActivityWithIntent(activity, url, defaultTitle);
            activity.startActivity(intent);
        } else {
            Intent intent = new Intent(activity, defaultClassToNavigate);
            activity.startActivity(intent);
        }
    }

}
