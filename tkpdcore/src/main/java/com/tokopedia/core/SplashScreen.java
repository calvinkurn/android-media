package com.tokopedia.core;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.interfaces.DefferedDeeplinkCallback;
import com.tokopedia.linker.model.LinkerDeeplinkData;
import com.tokopedia.linker.model.LinkerDeeplinkResult;
import com.tokopedia.linker.model.LinkerError;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import com.tokopedia.applink.ApplinkConst;

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

        fetchRemoteConfig();
    }

    private void fetchRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
        remoteConfig.fetch(getRemoteConfigListener());
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
        boolean status = GCMHandler.isPlayServicesAvailable(this);
        if(!status){
            Timber.w("P2#PLAY_SERVICE_ERROR#Problem with PlayStore | " + Build.FINGERPRINT+" | "+  Build.MANUFACTURER + " | "
                    + Build.BRAND + " | "+Build.DEVICE+" | "+Build.PRODUCT+ " | "+Build.MODEL
                    + " | "+Build.TAGS);
        }
        moveToHome();
    }

    protected void moveToHome() {
        Pgenerator = new PasswordGenerator(SplashScreen.this);
        InitNew();
        registerFCMDeviceID();
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
        CommonUtils.dumper(resultData);
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

    private void getBranchDefferedDeeplink() {
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
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getBranchDefferedDeeplink();
    }
}
