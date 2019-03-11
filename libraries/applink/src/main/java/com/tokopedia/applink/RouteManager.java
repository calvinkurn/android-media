package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author ricoharisin .
 * Central class for routing to activity
 */

public class RouteManager {

    /**
     * This will check the internal deeplink in the manifest
     * If the activity exists, it will route to that activity
     * Else, it will route to ApplinkRouter intent.
     * If still not supported will return null
     */
    public static @Nullable
    Intent getIntentInternal(@NonNull Context context, @NonNull String deeplink) {
        Uri uri = Uri.parse(deeplink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        } else {
            return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, deeplink);
        }
    }

    public static boolean isSupportApplinkInternal(@NonNull Context context, @NonNull String applink) {
        return getIntentInternal(context, applink) != null;
    }

    /**
     * use getIntentInternal instead
     */
    @Deprecated
    public static void route(Context context, String applink) {
        ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, applink);
    }

    /**
     * use getIntentInternal instead
     */
    @Deprecated
    public static Intent getIntent(Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, applink);
    }

    /**
     * use isSupportApplinkInternal instead
     */
    @Deprecated
    public static boolean isSupportApplink(Context context, String applink) {
        return ((ApplinkRouter) context.getApplicationContext()).isSupportApplink(applink);
    }

}
