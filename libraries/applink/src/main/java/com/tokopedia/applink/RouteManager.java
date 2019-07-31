package com.tokopedia.applink;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.tokopedia.config.GlobalConfig;

import java.util.List;
import java.util.Set;

/**
 * @author ricoharisin .
 * Central class for routing to activity
 */

public class RouteManager {

    private static final String EXTRA_APPLINK_UNSUPPORTED = "EXTRA_APPLINK_UNSUPPORTED";

    /**
     * will create implicit internal Intent ACTION_VIEW correspond to deeplink
     */
    private static Intent buildInternalImplicitIntent(@NonNull Context context, @NonNull String deeplink) {
        Uri uri = Uri.parse(deeplink);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        return intent;
    }

    /**
     * will return explicit Intent corresponds to deeplink that has been registered to Tokopedia internal app.
     * Deeplink should be registered in manifest.
     * If the deeplink triggered from customer app, it will not go to sellerapp, vice versa.
     * <p>
     * If multiple deeplink is registered in manifest, it will choose the first one.
     * <p>
     * DeepLinkHandlerActivity and DeepLinkActivity will be leaved out,
     * If the result is only DeepLinkHandlerActivity, it will return null
     * If the result are DeepLinkHandlerActivity, Activity B, Activity C, it will return activity B.
     *
     * <p>
     * Example:
     * (1) "tokopedia-android-internal://marketplace/product/{id}/review" will
     * return intent of ReviewProductActivity (if it is registered in ReviewProductActivity)
     * (2) "tokopedia://inbox" will return InboxActivity (if it is registered in InboxActivity)
     * (3) "tokopedia://wrongpath" will return null.
     * (4) "https://www.tokopedia.com" will return null.
     */
    private static @Nullable
    Intent buildInternalExplicitIntent(@NonNull Context context, @NonNull String deeplink) {
        Intent intent = buildInternalImplicitIntent(context, deeplink);

        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        if (resolveInfos == null || resolveInfos.size() == 0) {
            // intent cannot be viewed in app
            return null;
        } else {
            // remove deeplinkHandler and deeplinkActivity because it include all http:// and tokopedia://
            for (int i = resolveInfos.size() - 1; i >= 0; i--) {
                ResolveInfo resolveInfo = resolveInfos.get(i);
                String activityName = resolveInfo.activityInfo.name;
                if (GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME.equals(activityName) ||
                        GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME.equals(activityName)) {
                    resolveInfos.remove(i);
                }
            }

            // return the first intent only.
            if (resolveInfos.size() > 0) {
                Uri uri = Uri.parse(deeplink);
                Intent activityIntent = new Intent();
                activityIntent.setClassName(context.getPackageName(), resolveInfos.get(0).activityInfo.name);
                activityIntent.setData(uri);

                // copy the query Parameter to bundle
                Set<String> queryParameterNames = uri.getQueryParameterNames();
                for (String queryParameterName : queryParameterNames) {
                    activityIntent.putExtra(queryParameterName, uri.getQueryParameter(queryParameterName));
                }
                return activityIntent;
            } else {
                return null;
            }

        }
    }

    /**
     * route to the activity corresponds to the given applink.
     * Will do nothing if applink is not supported.
     */
    public static void route(Context context, String applinkPattern, String... parameter) {
        if (context == null) {
            return;
        }
        String uriString = UriUtil.buildUri(applinkPattern, parameter);
        if (uriString.isEmpty()) {
            return;
        }
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, uriString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = uriString;
        }
        Intent intent;
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(mappedDeeplink)) {
            ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, mappedDeeplink);
            return;
        } else if (URLUtil.isNetworkUrl(mappedDeeplink)) {
            intent = buildInternalImplicitIntent(context, mappedDeeplink);
            if (intent == null || intent.resolveActivity(context.getPackageManager()) == null) {
                intent = new Intent();
                intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
                intent.setData(Uri.parse(uriString));
                context.startActivity(intent);
            }
            context.startActivity(intent);
            return;
        } else {
            intent = buildInternalExplicitIntent(context, mappedDeeplink);
        }
        if (intent != null && intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    /**
     * route to the activity corresponds to the given applink.
     * Will route to Home if applink is not supported.
     */
    @Deprecated
    public static void routeWithFallback(Context context, String applinkPattern, String... parameter) {
        if (context == null) {
            return;
        }
        String uriString = UriUtil.buildUri(applinkPattern, parameter);
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, uriString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = uriString;
        }
        Intent intent;
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(mappedDeeplink)) {
            ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, mappedDeeplink);
            return;
        } else if (URLUtil.isNetworkUrl(mappedDeeplink)) {
            intent = buildInternalImplicitIntent(context, mappedDeeplink);
        } else {
            intent = buildInternalExplicitIntent(context, mappedDeeplink);
        }
        if (intent == null || intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent();
            intent.setClassName(context.getPackageName(), GlobalConfig.HOME_ACTIVITY_CLASS_NAME);
            intent.setData(Uri.parse(mappedDeeplink));
            intent.putExtra(EXTRA_APPLINK_UNSUPPORTED, true);
        } else {
            context.startActivity(intent);
        }
    }

    /**
     * return the intent for the given deeplink
     * If no activity found will return to home
     * <p>
     * See getIntentNoFallback if want to return null when no activity is found.
     */
    public static Intent getIntent(Context context, String deeplinkPattern, String... parameter) {
        String deeplink = UriUtil.buildUri(deeplinkPattern, parameter);
        Intent intent = getIntentNoFallback(context, deeplink);
        // set fallback for implicit intent

        if (intent == null || intent.resolveActivity(context.getPackageManager()) == null) {
            intent = new Intent();
            intent.setClassName(context.getPackageName(), GlobalConfig.HOME_ACTIVITY_CLASS_NAME);
            intent.setData(Uri.parse(deeplink));
            intent.putExtra(EXTRA_APPLINK_UNSUPPORTED, true);
        }
        return intent;
    }

    /**
     * return the intent for the deeplink
     * If no activity found will return null
     * <p>
     * See getIntent
     */
    public static @Nullable
    Intent getIntentNoFallback(Context context, String deeplinkPattern, String... parameter) {
        if (context == null) {
            return null;
        }
        String deeplink = UriUtil.buildUri(deeplinkPattern, parameter);
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, deeplink);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = deeplink;
        }
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(mappedDeeplink)) {
            return ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, mappedDeeplink);
        }
        if (URLUtil.isNetworkUrl(mappedDeeplink)) {
            return buildInternalImplicitIntent(context, mappedDeeplink);
        }
        return buildInternalExplicitIntent(context, mappedDeeplink);
    }

    /**
     * return true if applink is supported, either by airbnb or registered in manifest
     * <p>
     * http:// and https:// will always return true (will open webview)
     * tokopedia://wrongpath will return false.
     * <p>
     */
    public static boolean isSupportApplink(Context context, String applink) {
        if (context == null) {
            return false;
        }
        // check with airbnb
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(applink)) {
            return true;
        }
        if (URLUtil.isNetworkUrl(applink)) {
            return true;
        }
        return buildInternalExplicitIntent(context, applink) != null;
    }

    public static String routeWithAttribution(Context context, String applink,
                                              String trackerAttribution) {
        String attributionApplink;
        if (applink.contains("?")) {
            attributionApplink = applink + "&" + trackerAttribution;
        } else {
            attributionApplink = applink + "?" + trackerAttribution;
        }
        return attributionApplink;
    }
}
