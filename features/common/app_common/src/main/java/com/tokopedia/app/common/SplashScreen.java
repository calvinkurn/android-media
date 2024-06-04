package com.tokopedia.app.common;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.analytics.byteio.AppLogAnalytics;
import com.tokopedia.analytics.byteio.AppLogInterface;
import com.tokopedia.analytics.byteio.AppLogParam;
import com.tokopedia.analytics.byteio.EnterMethod;
import com.tokopedia.analytics.byteio.PageName;
import com.tokopedia.applink.AppUtil;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.kotlin.extensions.view.StringExtKt;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * modified by m.normansyah
 *
 * @since 3 Februari 2016
 * <p>
 * fetch some data from server in order to worked around.
 */
abstract public class SplashScreen extends AppCompatActivity implements AppLogInterface {

    @Override
    protected void onResume() {
        super.onResume();
        WeaveInterface moveToHomeFlowWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                boolean status = GCMHandler.isPlayServicesAvailable(SplashScreen.this);
                return executeMoveToHomeFlow(status);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(moveToHomeFlowWeave, RemoteConfigKey.ENABLE_ASYNC_MOVETOHOME, SplashScreen.this, true);
        moveToHome();
    }

    @NotNull
    private boolean executeMoveToHomeFlow(boolean status) {
        if (!status) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "splash_screen");
            messageMap.put("fingerprint", Build.FINGERPRINT);
            ServerLogger.log(Priority.P1, "PLAY_SERVICE_ERROR", messageMap);
        }
        registerFCMDeviceID(status);
        return true;
    }

    protected void moveToHome() {
        finishSplashScreen();
    }

    private GCMHandlerListener getGCMHandlerListener() {
        return regId -> {
        };
    }

    private void registerFCMDeviceID(boolean isPlayServiceAvailable) {
        GCMHandler gcm = new GCMHandler(this);
        gcm.actionRegisterOrUpdateDevice(getGCMHandlerListener(), isPlayServiceAvailable);
    }

    public abstract void finishSplashScreen();

    @NotNull
    public boolean getBranchDefferedDeeplink() {
        LinkerDeeplinkData linkerDeeplinkData = new LinkerDeeplinkData();
        linkerDeeplinkData.setReferrable(SplashScreen.this.getIntent().getData());
        linkerDeeplinkData.setActivity(SplashScreen.this);

        Map<String, String> additionalQueryParams = new HashMap<>();
        if (linkerDeeplinkData.getReferrable() != null) {
            for (String key : linkerDeeplinkData.getReferrable().getQueryParameterNames()) {
                additionalQueryParams.put(key, linkerDeeplinkData.getReferrable().getQueryParameter(key));
            }
        }

        LinkerManager.getInstance().handleDefferedDeeplink(LinkerUtils.createDeeplinkRequest(0,
                linkerDeeplinkData, new DefferedDeeplinkCallback() {
                    @Override
                    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
                        PersistentCacheManager.instance.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, linkerDefferedDeeplinkData.getPromoCode() != null ?
                                linkerDefferedDeeplinkData.getPromoCode() : "");
                        Uri.Builder deeplink = Uri.parse(linkerDefferedDeeplinkData.getDeeplink()).buildUpon();
                        if (!TextUtils.isEmpty(deeplink.toString())) {
                            if (!additionalQueryParams.isEmpty()) {
                                for (Map.Entry<String, String> set : additionalQueryParams.entrySet()) {
                                    deeplink.appendQueryParameter(set.getKey(), set.getValue());
                                }
                            }
                            navigateBranchDeeplink(deeplink.toString(), linkerDefferedDeeplinkData.getMinVersion());
                        }
                    }

                    @Override
                    public void onError(LinkerError linkerError) {
                    }
                }, SplashScreen.this));
        return true;
    }

    private void navigateBranchDeeplink(String deeplink, String minVersion) {
        String tokopediaDeeplink = deeplink;
        Intent intent = new Intent();
        boolean isUnderVersion = isUnderMinVersion(minVersion);
        if (isUnderVersion) {
            intent = RouteManager.getDeeplinkNotFoundIntent(SplashScreen.this);
            intent.putExtra("type", "update");
            intent.putExtra("source", "share");

            logLinker(deeplink, "update");
        } else {
            // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
            // because we need tracking UTM for those notification applink
            if (URLUtil.isNetworkUrl(deeplink)) {
                intent.setClassName(SplashScreen.this.getPackageName(),
                        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
                logLinker(deeplink, "open");
            } else {
                if (deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                    tokopediaDeeplink = deeplink;
                } else {
                    tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink;
                }
                //check if original url intent can be opened in app
                Intent intentCheck = RouteManager.getIntentNoFallback(SplashScreen.this, tokopediaDeeplink);
                if (intentCheck == null) {
                    intent = RouteManager.getDeeplinkNotFoundIntent(SplashScreen.this);
                    intent.putExtra("source", "share");
                    logLinker(tokopediaDeeplink, "404");
                } else {
                    intent.setClassName(SplashScreen.this.getPackageName(),
                            com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME);
                    logLinker(tokopediaDeeplink, "open");
                }
            }
        }
        intent.setData(Uri.parse(tokopediaDeeplink));
        // destroyed activity (SplashScreen) might not launch activity,
        // so better to use currentActivity instead.
        checkAppLogPDPExternalPromo(tokopediaDeeplink);
        boolean startFromCurrent = AppUtil.startActivityFromCurrentActivity(intent);
        if (!startFromCurrent) {
            startActivity(intent);
        }
        finish();
    }

    private void checkAppLogPDPExternalPromo(String tokopediaDeeplink) {
        try {
            Uri uri = Uri.parse(tokopediaDeeplink);
            AppLogAnalytics.INSTANCE.putEnterMethod(EnterMethod.CLICK_EXTERNAL_ADS);
            if (uri.getHost() != null
                    && uri.getHost().equals("product")
                    && uri.getPathSegments().size() == 1) {
                AppLogAnalytics.INSTANCE.putPageData(AppLogParam.ENTER_FROM, PageName.EXTERNAL_PROMO);
            }
        } catch (Exception e) {
        }
    }

    private void logLinker(String deeplink, String reason) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("reason", reason);
        messageMap.put("deeplink", deeplink);
        ServerLogger.logP2("LINKER", messageMap);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBranchDefferedDeeplink();
    }

    private Boolean isUnderMinVersion(String version) {
        try {
            String minVersionString = version.replaceAll("[^0-9]", "");
            String currentVersionString = GlobalConfig.VERSION_NAME.replaceAll("[^0-9]", "");
            int minVersionInt = StringExtKt.toIntOrZero(minVersionString);
            int currentVersionInt = StringExtKt.toIntOrZero(currentVersionString);
            if (minVersionString.length() < currentVersionString.length()) {
                int differenceLength = currentVersionString.length() - minVersionString.length();
                minVersionInt = minVersionInt * ((int) Math.pow(10, differenceLength));
            } else if (minVersionString.length() > currentVersionString.length()) {
                int differenceLength = currentVersionString.length() - minVersionString.length();
                currentVersionInt = currentVersionInt * ((int) Math.pow(10, differenceLength));
            }
            return currentVersionInt < minVersionInt;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @NonNull
    @Override
    public String getPageName() {
        return "";
    }

    @NonNull
    @Override
    public String getEnterFrom() {
        return getPageName();
    }

    @Override
    public boolean isEnterFromWhitelisted() {
        return false;
    }

    @Override
    public boolean isShadow() {
        return true;
    }

    @Override
    public boolean shouldTrackEnterPage() {
        return false;
    }
}
