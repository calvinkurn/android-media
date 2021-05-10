package com.tokopedia.applink;

import android.app.Activity;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tokopedia.analyticsdebugger.debugger.ApplinkLogger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.dev_monitoring_tools.userjourney.UserJourney;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.utils.uri.DeeplinkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * @author ricoharisin .
 * Central class for routing to activity
 */

public class RouteManager {

    private static final String EXTRA_APPLINK_UNSUPPORTED = "EXTRA_APPLINK_UNSUPPORTED";
    public static final String QUERY_PARAM = "QUERY_PARAM";
    private static final String LINK = ".link";

    public static final String BRANCH = "branch";
    public static final String BRANCH_FORCE_NEW_SESSION = "branch_force_new_session";

    public static final String KEY_REDIRECT_TO_SELLER_APP = "redirect_to_sellerapp";
    private static final String SELLER_APP_PACKAGE_NAME = "com.tokopedia.sellerapp";

    public static final String INTERNAL_VIEW = "com.tokopedia.internal.VIEW";
    public static final String DEFAULT_VIEW = "android.intent.action.VIEW";

    /**
     * will create implicit internal Intent ACTION_VIEW correspond to deeplink
     */
    private static Intent buildInternalImplicitIntent(@NonNull Context context, @NonNull String deeplink, String action) {
        ApplinkLogger.getInstance(context).appendTrace("Building implicit intent...");
        Uri uri = Uri.parse(deeplink);
        Intent intent = new Intent(action);
        intent.setData(uri);
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        String host = uri.getHost();
        if (host != null && host.contains(LINK)) {
            intent.putExtra(BRANCH, deeplink);
            intent.putExtra(BRANCH_FORCE_NEW_SESSION, true);
        }
        ApplinkLogger.getInstance(context).appendTrace("Implicit intent result:\n" + intent.toString());
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
        ApplinkLogger.getInstance(context).appendTrace("Building explicit intent...");
        Uri uri = Uri.parse(deeplink);
        if (uri.isOpaque()) {
            return null;
        }
        Intent intent = buildInternalImplicitIntent(context, deeplink, INTERNAL_VIEW);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        final boolean shouldRedirectToSellerApp = uri.getBooleanQueryParameter(KEY_REDIRECT_TO_SELLER_APP, false);

        if (shouldRedirectToSellerApp && !GlobalConfig.isSellerApp()) {
            return getIntentRedirectSellerApp(context, uri);
        } else if (resolveInfos.size() == 0) {
            // intent cannot be viewed in app
            ApplinkLogger.getInstance(context).appendTrace("Intent cannot be viewed in app");
            ApplinkLogger.getInstance(context).appendTrace("Explicit intent result:\nnull");
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
                Intent activityIntent = new Intent();
                activityIntent.setClassName(context.getPackageName(), resolveInfos.get(0).activityInfo.name);
                activityIntent.setData(uri);

                // copy the query Parameter to bundle
                Set<String> queryParameterNames = uri.getQueryParameterNames();
                for (String queryParameterName : queryParameterNames) {
                    activityIntent.putExtra(queryParameterName, uri.getQueryParameter(queryParameterName));
                }
                ApplinkLogger.getInstance(context).appendTrace("Explicit intent result:\n" + activityIntent.toString());
                return activityIntent;
            } else {
                ApplinkLogger.getInstance(context).appendTrace("No ResolveInfo Found");
                ApplinkLogger.getInstance(context).appendTrace("Explicit intent result:\nnull");
                return null;
            }

        }
    }

    /**
     * Create intent redirect to sellerapp
     * If sellerapp not installed yet then open sellerapp on google playstore
     */
    private static Intent getIntentRedirectSellerApp(Context context, Uri uri) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(SELLER_APP_PACKAGE_NAME);
        if (null != intent) {
            intent.setData(uri);
            return intent;
        } else {
            return getIntentToPlayStore(SELLER_APP_PACKAGE_NAME);
        }
    }

    private static Intent getIntentToPlayStore(String packageName) {
        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        } catch (ActivityNotFoundException e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
        }
    }

    /**
     * Create a new instance of a Fragment with the given class name.
     *
     * @param activity
     * @param className
     * @return
     */
    public static Fragment instantiateFragment(@NonNull AppCompatActivity activity, @NonNull String className, @Nullable Bundle extras) {
        if(isClassExist(className)) {
            Fragment fragment = activity.getSupportFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), className);
            if (extras != null) {
                fragment.setArguments(extras);
            }
            return fragment;
        } else {
            return null;
        }
    }

    private static boolean isClassExist(String className) {
        try  {
            Class.forName(className);
            return true;
        }  catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * route to the activity corresponds to the given applink.
     * Will do nothing if applink is not supported.
     *
     * @return true if successfully routing to activity
     */
    public static boolean route(Context context, String applinkPattern, String... parameter) {
        Bundle bundle = getBundleFromAppLinkQueryParams(applinkPattern);
        bundle.putBundle(QUERY_PARAM, bundle);
        return route(context, new Bundle(), applinkPattern, parameter);
    }

    public static boolean route(Context context, Bundle queryParamBundle, String applinkPattern, String... parameter) {
        if (context == null) {
            return false;
        }

        String uriString = UriUtil.buildUri(applinkPattern, parameter);
        if (uriString.isEmpty()) {
            return false;
        }
        ApplinkLogger.getInstance(context).startTrace(uriString);
        ApplinkLogger.getInstance(context).appendTrace("Start Routing...");

        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, uriString);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = uriString;
        }
        ApplinkLogger.getInstance(context).appendTrace("Mapped Deeplink:\n" + mappedDeeplink);

        Intent intent;
        String dynamicFeatureDeeplink = DeeplinkDFMapper.getDFDeeplinkIfNotInstalled(context, mappedDeeplink);
        ApplinkLogger.getInstance(context).appendTrace("DF Deeplink:\n" + dynamicFeatureDeeplink);

        if (dynamicFeatureDeeplink != null) {
            ApplinkLogger.getInstance(context).appendTrace("Building DF intent");
            intent = buildInternalExplicitIntent(context, dynamicFeatureDeeplink);
        } else if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(mappedDeeplink)) {
            ApplinkLogger.getInstance(context).appendTrace("AirBnB deeplink supported, redirect to AirBnB deeplink handler");
            ApplinkLogger.getInstance(context).save();

            Uri uri = Uri.parse(mappedDeeplink);
            boolean shouldRedirectToSellerApp = uri.getBooleanQueryParameter(KEY_REDIRECT_TO_SELLER_APP, false);
            if (shouldRedirectToSellerApp && !GlobalConfig.isSellerApp()) { //create intent to redirect to sellerapp
                intent = buildInternalExplicitIntent(context, mappedDeeplink);
            } else {
                if (context instanceof Activity) {
                    ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity((Activity) context, mappedDeeplink, queryParamBundle);
                } else {
                    ((ApplinkRouter) context.getApplicationContext()).goToApplinkActivity(context, mappedDeeplink);
                }
                return true;
            }
        } else if (URLUtil.isNetworkUrl(mappedDeeplink)) {
            ApplinkLogger.getInstance(context).appendTrace("Network url detected");
            intent = buildInternalImplicitIntent(context, mappedDeeplink, DEFAULT_VIEW);
            if (intent.resolveActivity(context.getPackageManager()) == null) {
                intent = new Intent();
                intent.setClassName(context.getPackageName(), GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
                intent.setData(Uri.parse(uriString));
                ApplinkLogger.getInstance(context).appendTrace("Create intent to DeeplinkActivity");
            }
            if (queryParamBundle != null) {
                intent.putExtras(queryParamBundle);
            }

            ComponentName destActivity = intent.resolveActivity(context.getPackageManager());
            if (destActivity != null) {
                ApplinkLogger.getInstance(context).appendTrace("Starting activity:\n" + destActivity.getClassName());
            } else {
                ApplinkLogger.getInstance(context).appendTrace("Starting activity");
            }
            ApplinkLogger.getInstance(context).save();
            context.startActivity(intent);
            return true;
        } else {
            intent = buildInternalExplicitIntent(context, mappedDeeplink);
        }

        if (intent != null && intent.resolveActivity(context.getPackageManager()) != null) {
            if (queryParamBundle != null) {
                intent.putExtras(queryParamBundle);
            }
            ApplinkLogger.getInstance(context).appendTrace("Starting activity:\n"
                    + intent.resolveActivity(context.getPackageManager()).getClassName());
            ApplinkLogger.getInstance(context).save();
            context.startActivity(intent);
            return true;
        } else {
            logErrorOpenDeeplink(context, uriString);
        }

        ApplinkLogger.getInstance(context).appendTrace("Error: No destination activity found");
        ApplinkLogger.getInstance(context).save();
        return false;
    }

    private static void logErrorOpenDeeplink(Context context, String uriString) {
        try {
            String sourceClass = "";
            String referrer = "";
            if (context instanceof Activity) {
                sourceClass = ((Activity) context).getClass().getCanonicalName();
                referrer = DeeplinkUtils.INSTANCE.getReferrerCompatible((Activity) context);
            } else if (context instanceof Service) {
                sourceClass = ((Service) context).getClass().getCanonicalName();
            }
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "Router");
            messageMap.put("source", sourceClass);
            messageMap.put("referrer", referrer);
            messageMap.put("uri", uriString);
            messageMap.put("journey", UserJourney.INSTANCE.getReadableJourneyActivity(5));
            ServerLogger.log(Priority.P1, "APPLINK_OPEN_ERROR", messageMap);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public static Bundle getBundleFromAppLinkQueryParams(String mappedDeeplink) {
        if (TextUtils.isEmpty(mappedDeeplink)) return new Bundle();
        Map<String, String> map = UriUtil.uriQueryParamsToMap(mappedDeeplink);
        Bundle bundle = new Bundle();
        for (String key : map.keySet()) {
            String value = map.get(key);
            bundle.putString(key, value);
        }
        return bundle;
    }

    public static Bundle getBundleFromAppLinkQueryParams(Uri uri) {
        Bundle bundle = new Bundle();
        if (uri != null && !TextUtils.isEmpty(uri.toString())) {
            bundle = getBundleFromAppLinkQueryParams(uri.toString());
        }
        return bundle;
    }

    public static void putQueryParamsInIntent(Intent intent, String mappedDeeplink) {
        Map<String, String> map = UriUtil.uriQueryParamsToMap(mappedDeeplink);
        for (String key : map.keySet()) {
            String value = map.get(key);
            intent.putExtra(key, value);
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
            logErrorOpenDeeplink(context, deeplink);
            intent = getHomeIntent(context);
            intent.setData(Uri.parse(deeplink));
            intent.putExtra(EXTRA_APPLINK_UNSUPPORTED, true);
        }
        return intent;
    }

    public static Intent getIntent(Context context, String deeplinkPattern, Uri orignUri, String... parameter) {
        Intent intent = getIntent(context, deeplinkPattern, parameter);

        Bundle queryParamBundle = RouteManager.getBundleFromAppLinkQueryParams(orignUri);
        Bundle defaultBundle = new Bundle();
        defaultBundle.putBundle(RouteManager.QUERY_PARAM, queryParamBundle);
        intent.putExtras(defaultBundle);
        return intent;
    }

    private static Intent getHomeIntent(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context.getPackageName(), GlobalConfig.HOME_ACTIVITY_CLASS_NAME);
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
        ApplinkLogger.getInstance(context).startTrace(deeplink);
        ApplinkLogger.getInstance(context).appendTrace("Start getting intent...");

        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, deeplink);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = deeplink;
        }
        ApplinkLogger.getInstance(context).appendTrace("Mapped Deeplink:\n" + mappedDeeplink);

        String dynamicFeatureDeeplink = DeeplinkDFMapper.getDFDeeplinkIfNotInstalled(context, mappedDeeplink);
        ApplinkLogger.getInstance(context).appendTrace("DF Deeplink:\n" + dynamicFeatureDeeplink);

        if (dynamicFeatureDeeplink != null) {
            Intent dfIntent = buildInternalExplicitIntent(context, dynamicFeatureDeeplink);
            ApplinkLogger.getInstance(context).appendTrace("Returning DF intent:\n" + (dfIntent != null ? dfIntent.toString() : ""));
            ApplinkLogger.getInstance(context).save();
            return dfIntent;
        }
        if (((ApplinkRouter) context.getApplicationContext()).isSupportApplink(mappedDeeplink)) {
            ApplinkLogger.getInstance(context).appendTrace("AirBnB deeplink supported, redirect to AirBnB deeplink handler");
            Intent intent = ((ApplinkRouter) context.getApplicationContext()).getApplinkIntent(context, mappedDeeplink);
            ApplinkLogger.getInstance(context).appendTrace("Returning AirBnB intent:\n" + (intent != null ? intent.toString() : ""));
            ApplinkLogger.getInstance(context).save();
            return intent;
        }
        if (URLUtil.isNetworkUrl(mappedDeeplink)) {
            ApplinkLogger.getInstance(context).appendTrace("Network url detected");
            Intent intent = buildInternalImplicitIntent(context, mappedDeeplink, DEFAULT_VIEW);
            Intent webIntent;
            if (intent.resolveActivity(context.getPackageManager()) == null) {
                webIntent = null;
            } else {
                webIntent = intent;
            }
            ApplinkLogger.getInstance(context).appendTrace("Returning network intent:\n" + (webIntent != null ? webIntent.toString() : ""));
            ApplinkLogger.getInstance(context).save();
            return webIntent;
        }
        Intent intent = buildInternalExplicitIntent(context, mappedDeeplink);
        ApplinkLogger.getInstance(context).appendTrace("Returning intent:\n" + (intent != null ? intent.toString() : ""));
        ApplinkLogger.getInstance(context).save();
        return intent;
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
        String mappedDeeplink = DeeplinkMapper.getRegisteredNavigation(context, applink);
        if (TextUtils.isEmpty(mappedDeeplink)) {
            mappedDeeplink = applink;
        }
        String dynamicFeatureDeeplink = DeeplinkDFMapper.getDFDeeplinkIfNotInstalled(context, mappedDeeplink);
        if (dynamicFeatureDeeplink != null) {
            return buildInternalExplicitIntent(context, dynamicFeatureDeeplink) != null;
        }
        return buildInternalExplicitIntent(context, mappedDeeplink) != null;
    }

    public static void routeNoFallbackCheck(Context context, String applink, String url) {
        Intent intent = getIntentNoFallback(context, applink);
        if (applink != null && intent != null) {
            context.startActivity(intent);
        } else {
            route(context, url);
        }
    }


}
