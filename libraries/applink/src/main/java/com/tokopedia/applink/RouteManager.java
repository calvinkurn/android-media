package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;

/**
 * @author ricoharisin .
 * Central class for routing to acrtivity
 */

public class RouteManager {

    public static void route(Context context, String applink) {
        ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, applink);
    }

    public static Intent getIntent(Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, applink);
    }

    public static boolean isSupportApplink(Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).isSupportApplink(applink);
    }

    public static void routeWithAttribution(Context context, String applink,
                                            String trackerAttribution) {
        String attributionApplink;
        if (applink.contains("?")) {
            attributionApplink = applink + "&"+ trackerAttribution;
        } else {
            attributionApplink = applink + "?"+ trackerAttribution;
        }

        ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, attributionApplink);
    }
}
