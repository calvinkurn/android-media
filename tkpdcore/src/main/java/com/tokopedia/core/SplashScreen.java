package com.tokopedia.core;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import androidx.appcompat.app.AppCompatActivity;

import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;
import com.tokopedia.weaver.WeaverFirebaseConditionCheck;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;


/**
 * modified by m.normansyah
 *
 * @since 3 Februari 2016
 * <p>
 * fetch some data from server in order to worked around.
 */
public class SplashScreen extends AppCompatActivity implements DownloadResultReceiver.Receiver{

    public static final int DAYS_IN_SECONDS = 86400;
    public static final int STATUS_FINISHED = 1;
    public static final String SHIPPING_CITY_DURATION_STORAGE = "shipping_city_storage";

    private PasswordGenerator Pgenerator;
    String id = null;
    protected View decorView;

    protected RemoteConfig remoteConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        resetAllDatabaseFlag();
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return fetchRemoteConfig();
            }
        };
        Weaver.Companion.executeWeaveCoRoutine(remoteConfigWeave, new WeaverFirebaseConditionCheck(RemoteConfigKey.ENABLE_ASYNC_REMOTECONF_FETCH, remoteConfig));
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
        WeaveInterface branchDefferedDeeplinkWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return getBranchDefferedDeeplink();
            }
        };
        Weaver.Companion.executeWeaveCoRoutine(branchDefferedDeeplinkWeave, new WeaverFirebaseConditionCheck(RemoteConfigKey.ENABLE_ASYNC_DEFFERED_DEEPLINK_FETCH, remoteConfig));
    }

    @Override
    protected void onResume() {
        super.onResume();
        WeaveInterface moveToHomeFlowWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return executeMoveToHomeFlow();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(moveToHomeFlowWeave, RemoteConfigKey.ENABLE_ASYNC_MOVETOHOME, SplashScreen.this);
        moveToHome();
    }

    @NotNull
    private boolean executeMoveToHomeFlow(){
        boolean status = GCMHandler.isPlayServicesAvailable(this);
        if(!status){
            Timber.w("P2#PLAY_SERVICE_ERROR#Problem with PlayStore | " + Build.FINGERPRINT+" | "+  Build.MANUFACTURER + " | "
                    + Build.BRAND + " | "+Build.DEVICE+" | "+Build.PRODUCT+ " | "+Build.MODEL
                    + " | "+Build.TAGS);
        }
        Pgenerator = new PasswordGenerator(SplashScreen.this);
        InitNew();
        registerFCMDeviceID();
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

    private void registerFCMDeviceID() {
        GCMHandler gcm = new GCMHandler(this);
        gcm.actionRegisterOrUpdateDevice(getGCMHandlerListener());
    }

    public void finishSplashScreen() {
        Intent intent = HomeRouter.getHomeActivity(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        Timber.d(resultData.toString());
        if (resultCode == STATUS_FINISHED) finishSplashScreen();
    }

    private void resetAllDatabaseFlag() {
        LocalCacheHandler flagDB = new LocalCacheHandler(this, "DATABASE_VERSION" + MainApplication.DATABASE_VERSION);
        if (!flagDB.getBoolean("reset_db_flag", false)) {
            LocalCacheHandler.clearCache(this, SHIPPING_CITY_DURATION_STORAGE);
            if (getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) getApplication()).resetAddProductCache(this);
            }
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
        if(LinkerManager.getInstance() == null){
            initLinker();
        }
        LinkerDeeplinkData linkerDeeplinkData = new LinkerDeeplinkData();
        linkerDeeplinkData.setClientId(TrackingUtils.getClientID(this));
        linkerDeeplinkData.setReferrable(this.getIntent().getData());
        linkerDeeplinkData.setActivity(this);

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
                            intent.setData(Uri.parse(tokopediaDeeplink));
                            startActivity(intent);
                            finish();
                        }
                    }


                    @Override
                    public void onError(LinkerError linkerError) {
                    }
                }, this));
        return true;
    }

    private void initLinker(){
        LinkerManager.initLinkerManager(getApplicationContext()).setGAClientId(TrackingUtils.getClientID(SplashScreen.this.getApplicationContext()));
        UserSession userSession = new UserSession(SplashScreen.this);

        if(userSession.isLoggedIn()) {
            UserData userData = new UserData();
            userData.setUserId(userSession.getUserId());

            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY,
                    userData));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        WeaveInterface branchDefferedDeeplinkWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return getBranchDefferedDeeplink();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(branchDefferedDeeplinkWeave, RemoteConfigKey.ENABLE_ASYNC_DEFFERED_DEEPLINK_FETCH, SplashScreen.this);
    }
}
