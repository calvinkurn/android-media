package com.tokopedia.app.common;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import androidx.appcompat.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * modified by m.normansyah
 *
 * @since 3 Februari 2016
 * <p>
 * fetch some data from server in order to worked around.
 */
public class SplashScreen extends AppCompatActivity {

    public static final int DATABASE_VERSION = 7;
    public static final String SHIPPING_CITY_DURATION_STORAGE = "shipping_city_storage";
    private Iris irisAnalytics;
    public static final String IRIS_PRE_INSTALL_WEAVE = "android_async_preinstall_iris_event";
    private boolean isAppFirstInstall;

    protected View decorView;

    protected RemoteConfig remoteConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAppFirstInstall = isFirstInstall();
        if(isAppFirstInstall) {
            sendCampaignPreInstallTOGTM();
        }
        sendIrisPreInstallEvent(this);
        resetAllDatabaseFlag();
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return fetchRemoteConfig();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(remoteConfigWeave, RemoteConfigKey.ENABLE_ASYNC_REMOTECONF_FETCH, getApplicationContext(), true);
    }

    @NotNull
    private boolean fetchRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
        remoteConfig.fetch(getRemoteConfigListener());
        return true;
    }

    protected RemoteConfig.Listener getRemoteConfigListener(){
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean status = GCMHandler.isPlayServicesAvailable(SplashScreen.this);
        WeaveInterface moveToHomeFlowWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeMoveToHomeFlow(status);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(moveToHomeFlowWeave, RemoteConfigKey.ENABLE_ASYNC_MOVETOHOME, SplashScreen.this, true);
        moveToHome();
    }

    @NotNull
    private boolean executeMoveToHomeFlow(boolean status){
        if(!status){
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

    public void finishSplashScreen() {
        Intent intent = RouteManager.getIntent(this, ApplinkConst.HOME);
        startActivity(intent);
        finish();
    }

    private void resetAllDatabaseFlag() {
        LocalCacheHandler flagDB = new LocalCacheHandler(this, "DATABASE_VERSION" + DATABASE_VERSION);
        if (!flagDB.getBoolean("reset_db_flag", false)) {
            LocalCacheHandler.clearCache(this, SHIPPING_CITY_DURATION_STORAGE);
        }

        flagDB.putBoolean("reset_db_flag", true);
        flagDB.applyEditor();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @NotNull
    public boolean getBranchDefferedDeeplink() {
        LinkerDeeplinkData linkerDeeplinkData = new LinkerDeeplinkData();
        linkerDeeplinkData.setReferrable(SplashScreen.this.getIntent().getData());
        linkerDeeplinkData.setActivity(SplashScreen.this);

        LinkerManager.getInstance().handleDefferedDeeplink(LinkerUtils.createDeeplinkRequest(0,
                linkerDeeplinkData, new DefferedDeeplinkCallback() {
                    @Override
                    public void onDeeplinkSuccess(LinkerDeeplinkResult linkerDefferedDeeplinkData) {
                        PersistentCacheManager.instance.put(TkpdCache.Key.KEY_CACHE_PROMO_CODE, linkerDefferedDeeplinkData.getPromoCode() != null ?
                                linkerDefferedDeeplinkData.getPromoCode() : "");
                        String deeplink = linkerDefferedDeeplinkData.getDeeplink();
                        if (!TextUtils.isEmpty(deeplink)) {
                            // Notification will go through DeeplinkActivity and DeeplinkHandlerActivity
                            // because we need tracking UTM for those notification applink
                            String tokopediaDeeplink = deeplink;
                            Intent intent = new Intent();
                            if (URLUtil.isNetworkUrl(deeplink)) {
                                intent.setClassName(SplashScreen.this.getPackageName(),
                                        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
                            } else {
                                if (deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                                    tokopediaDeeplink = deeplink;
                                } else {
                                    tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink;
                                }
                                intent.setClassName(SplashScreen.this.getPackageName(),
                                        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME);
                            }
                            Map<String, String> messageMap = new HashMap<>();
                            messageMap.put("type", "splash_screen");
                            messageMap.put("deeplink", tokopediaDeeplink);
                            ServerLogger.log(Priority.P2, "LINKER", messageMap);
                            intent.setData(Uri.parse(tokopediaDeeplink));
                            startActivity(intent);
                            finish();
                        }
                    }


                    @Override
                    public void onError(LinkerError linkerError) {
                    }
                }, SplashScreen.this));
        return true;
    }

    private void sendIrisPreInstallEvent(Context context){
        if(GlobalConfig.IS_PREINSTALL) {
            WeaveInterface irisPreInstallWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    return executeSendIrisEvent(context);
                }
            };
            Weaver.Companion.executeWeaveCoRoutineWithFirebase(irisPreInstallWeave, IRIS_PRE_INSTALL_WEAVE, getApplicationContext(), true);
        }
    }

    private boolean executeSendIrisEvent(Context context){
        HashMap<String, Object> value = new HashMap<>();
        value.put("partner_source", "oppopreinstallof");
        value.put("Partner_Referred", "source_appmarket/phonepreinstall");
        value.put("eventaction", "appOpen");
        value.put("eventcategory", "preInstall");
        value.put("eventlabel", "oppo");
        if(isAppFirstInstall) {
            value.put("event", "first_open");
        }
        else {
            value.put("event", "recurring_open");
        }
        getIrisAnalytics(context).sendEvent(value);
        return true;
    }

    private Iris getIrisAnalytics(Context context){
        if(irisAnalytics == null){
            irisAnalytics = IrisAnalytics.getInstance(context);
        }
        return irisAnalytics;
    }

    private boolean isFirstInstall() {
        SharedPreferences sharedPrefs = this.getSharedPreferences(
                "KEY_FIRST_INSTALL_SEARCH", Context.MODE_PRIVATE);
        long firstInstallCacheValue = sharedPrefs.getLong(
                "KEY_IS_FIRST_INSTALL_TIME_PRE_INSTALL", 0);
        if (firstInstallCacheValue == 0L) {
            saveFirstInstallTime();
            return true;
        }
        Date now = new Date();
        Date firstInstallTime = new Date(firstInstallCacheValue);
        if (now.compareTo(firstInstallTime) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    private void saveFirstInstallTime() {
        Date date = new Date();
        SharedPreferences sharedPrefs = this.getSharedPreferences(
                "KEY_FIRST_INSTALL_SEARCH", Context.MODE_PRIVATE);
        sharedPrefs.edit().putLong(
                "KEY_IS_FIRST_INSTALL_TIME_PRE_INSTALL", date.getTime()).apply();
    }

    public void sendCampaignPreInstallTOGTM(){
        if(GlobalConfig.IS_PREINSTALL) {
            Map<String, Object> param = new HashMap<>();
            param.put(AppEventTracking.GTM.UTM_SOURCE, "oppo_int");
            param.put(AppEventTracking.GTM.UTM_MEDIUM, "notset");
            param.put("screenName", "SplashScreen");
            param.put(AppEventTracking.GTM.UTM_CAMPAIGN, "oppopreinstallof-dp_int-tp-10001511-0000-alon-alon");
            TrackApp.getInstance().getGTM().sendCampaign(param);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBranchDefferedDeeplink();
    }
}
