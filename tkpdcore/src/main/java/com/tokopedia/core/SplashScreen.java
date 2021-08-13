package com.tokopedia.core;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import androidx.appcompat.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.var.TkpdCache;
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
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


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

    private PasswordGenerator Pgenerator;
    protected View decorView;

    protected RemoteConfig remoteConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resetAllDatabaseFlag();
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return fetchRemoteConfig();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(remoteConfigWeave, RemoteConfigKey.ENABLE_ASYNC_REMOTECONF_FETCH, getApplicationContext());
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
    protected void onStart() {
        super.onStart();
        getBranchDefferedDeeplink();
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(moveToHomeFlowWeave, RemoteConfigKey.ENABLE_ASYNC_MOVETOHOME, SplashScreen.this);
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
        Pgenerator = new PasswordGenerator(SplashScreen.this);
        InitNew();
        registerFCMDeviceID(status);
        return true;
    }

    protected void moveToHome() {
        finishSplashScreen();
    }

    private void InitNew() {
        if (Pgenerator.getAppId() == null) {
            Pgenerator.generateAPPID(status -> {

            });
        }
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
        Intent intent = ((com.tokopedia.core.TkpdCoreRouter) getApplicationContext()).getHomeIntent(this);
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
    private boolean getBranchDefferedDeeplink() {
        LinkerDeeplinkData linkerDeeplinkData = new LinkerDeeplinkData();
        linkerDeeplinkData.setClientId(TrackingUtils.getClientID(SplashScreen.this));
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
                            String tokopediaDeeplink;
                            if (deeplink.startsWith(ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://")) {
                                tokopediaDeeplink = deeplink;
                            } else {
                                tokopediaDeeplink = ApplinkConst.APPLINK_CUSTOMER_SCHEME + "://" + deeplink;
                            }
                            Intent intent = new Intent();
                            if (URLUtil.isNetworkUrl(tokopediaDeeplink)) {
                                intent.setClassName(SplashScreen.this.getPackageName(),
                                        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME);
                            } else {
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBranchDefferedDeeplink();
    }
}
