package com.tokopedia.core;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.PasswordGenerator.PGListener;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

/**
 * modified by m.normansyah
 *
 * @since 3 Februari 2016
 * <p>
 * fetch some data from server in order to worked around.
 */
public class SplashScreen extends AppCompatActivity implements DownloadResultReceiver.Receiver {
    public static final int TIME_DELAY = 300;
    public static final String IS_LOADING = "IS_LOADING";
    public static final String RE_INIT_DATA_FOR_THE_FIRST_TIME = "RE-INIT-DATA-FOR-THE-FIRST-TIME";
    public static final int WEEK_IN_SECONDS = 604800;
    public static final int DAYS_IN_SECONDS = 86400;
    public static final int OVERLAY_PERMISSION_REQ_CODE = 1080;
    private PasswordGenerator Pgenerator;
    DownloadResultReceiver mReceiver;
    String id = null;
    protected SessionHandler sessionHandler;
    protected View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        sessionHandler = new SessionHandler(this);
        resetAllDatabaseFlag();
        initPermissionReactNativeDev();
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
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        remoteConfig.fetch(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new DiscoveryInteractorImpl().editProductDetail(this, "45469593", "", "");
        //  moveToHome();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleBranchDefferedDeeplink();
    }

    private void moveToHome() {
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        Pgenerator = new PasswordGenerator(SplashScreen.this);
        InitNew();
        registerFCMDeviceID();
        finishSplashScreen();
    }
//        }, TIME_DELAY);


    private void InitNew() {
        if (Pgenerator.getAppId() == null) {

            //message.setText(R.string.title_first_time);
            Pgenerator.generateAPPID(new PGListener() {

                @Override
                public void onSuccess(int status) {
//                    finishSplashScreen();

                }
            });
        } else {
//            finishSplashScreen();
        }
    }

    private GCMHandlerListener getGCMHandlerListener() {
        return new GCMHandlerListener() {
            @Override
            public void onGCMSuccess(String regId) {
                //bypassV2Login();
            }
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
        if (resultCode == DownloadService.STATUS_FINISHED) finishSplashScreen();
    }

    private void resetAllDatabaseFlag() {
        LocalCacheHandler flagDB = new LocalCacheHandler(this, "DATABASE_VERSION" + MainApplication.DATABASE_VERSION);
        if (!flagDB.getBoolean("reset_db_flag", false)) {
            LocalCacheHandler.clearCache(this, DataManagerImpl.SHIPPING_CITY_DURATION_STORAGE);
            if (getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) getApplication()).resetAddProductCache(this);
            }
        }

        flagDB.putBoolean("reset_db_flag", true);
        flagDB.applyEditor();
    }

    private void initPermissionReactNativeDev() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                }
            }
        }
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

    private void handleBranchDefferedDeeplink() {
        Branch branch = Branch.getInstance();
        if (branch == null){
            moveToHome();
        }else {
            branch.setRequestMetadata("$google_analytics_client_id", TrackingUtils.getClientID());
            branch.initSession(new Branch.BranchReferralInitListener() {
                @Override
                public void onInitFinished(JSONObject referringParams, BranchError error) {
                    if (error == null) {
                        CommonUtils.dumper(referringParams.toString());
                        try {
                            String deeplink = referringParams.getString("$android_deeplink_path");
                            Uri uri = Uri.parse(Constants.Schemes.APPLINKS + "://" + deeplink);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            moveToHome();

                        }
                    } else {
                        moveToHome();
                    }
                }
            }, this.getIntent().getData(), this);
        }
    }

}
