package com.tokopedia.app.common;


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
import com.tokopedia.applink.RouteManager;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.logger.LogManager;
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


/**
 * modified by m.normansyah
 *
 * @since 3 Februari 2016
 * <p>
 * fetch some data from server in order to worked around.
 */
public class SplashScreen extends AppCompatActivity {

    protected RemoteConfig remoteConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return fetchRemoteConfig();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineNow(remoteConfigWeave);
    }

    @NotNull
    private boolean fetchRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
        remoteConfig.fetch(getRemoteConfigListener());
        return true;
    }

    private RemoteConfig.Listener getRemoteConfigListener() {
        return new RemoteConfig.Listener() {
            @Override
            public void onComplete(RemoteConfig remoteConfig) {
                LogManager logManager = LogManager.instance;
                if (logManager != null) {
                    logManager.refreshConfig();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

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

    public void finishSplashScreen() {
        Intent intent = RouteManager.getIntent(this, ApplinkConst.HOME);
        startActivity(intent);
        finish();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBranchDefferedDeeplink();
    }
}
