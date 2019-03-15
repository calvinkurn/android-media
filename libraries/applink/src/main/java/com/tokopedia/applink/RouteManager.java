package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author ricoharisin .
 * Central class for routing to activity
 *
 * This will check the deeplink in the manifest
 * If the activity exists, it will route to that activity
 * Else, it will route to ApplinkRouter intent.
 * If still not supported will return null
 */

public class RouteManager {

    private static Intent buildInternalUri(@NonNull Context context, @NonNull String deeplink) {
        Uri uri = Uri.parse(deeplink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        return intent;
    }

    public static void route(Context context, String applink) {
        Intent intent = RouteManager.getIntent(context, applink);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, applink);
        }
    }

    public static Intent getIntent(Context context, String applink) {
        Intent intent = buildInternalUri(context, applink);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        } else {
            return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, applink);
        }
    }

    public static boolean isSupportApplink(Context context, String applink) {
        return buildInternalUri(context, applink).resolveActivity(context.getPackageManager()) != null ||
                ((ApplinkRouter) context.getApplicationContext()).isSupportApplink(applink);
    }

    public static String routeWithAttribution(Context context, String applink,
                                              String trackerAttribution) {
        String attributionApplink;
        if (applink.contains("?")) {
            attributionApplink = applink + "&" + trackerAttribution;
        } else {
            attributionApplink = applink + "?" + trackerAttribution;
        }

//        ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, attributionApplink);
        return attributionApplink;
    }
}
