package com.tokopedia.applink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author ricoharisin .
 *
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
}
