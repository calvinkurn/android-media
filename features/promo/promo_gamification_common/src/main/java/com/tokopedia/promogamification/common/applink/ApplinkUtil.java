package com.tokopedia.promogamification.common.applink;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.promogamification.common.R;

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
            RouteManager.route(activity, ApplinkConstInternalGlobal.WEBVIEW_TITLE, defaultTitle, url);
        } else {
            Intent intent = new Intent(activity, defaultClassToNavigate);
            activity.startActivity(intent);
        }
    }

}
