package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author ricoharisin .
 *
 * Central class for routing to acrtivity
 */

public class RouteManager {

    public static void route(@NonNull Context context, String applink) {
        ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, applink);
    }

    public static Intent getIntent(@NonNull Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, applink);
    }

    public static boolean isSupportApplink(@NonNull Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).isSupportApplink(applink);
    }
}
