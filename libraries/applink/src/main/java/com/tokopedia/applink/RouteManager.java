package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;

import static com.tokopedia.applink.ProductDetailRouteManager.getProductIntent;

/**
 * @author ricoharisin .
 * Central class for routing to activity
 * <p>
 * This will check the deeplink in the manifest
 * If the activity exists, it will route to that activity
 * Else, it will route to ApplinkRouter intent.
 * If still not supported will return null
 */

public class RouteManager {

    static Intent buildInternalUri(@NonNull Context context, @NonNull String deeplink) {
        Uri uri = Uri.parse(deeplink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        return intent;
    }

    public static void route(Context context, String applinkPattern, String... parameter) {
        String uriString = UriUtil.buildUri(applinkPattern, parameter);
        Intent intent = getIntent(context, uriString);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, uriString);
        }
    }

    public static Intent getIntent(Context context, String applinkPattern, String... parameter) {
        String applink = UriUtil.buildUri(applinkPattern, parameter);
        // TEMPORARY SOLUTION TO route product to old PDP by remote config
        Intent intent;
        if (ProductDetailRouteManager.isProductApplink(applink)) {
            intent = getProductIntent(context, applink);
        } else {
            intent = buildInternalUri(context, applink);
        }
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
