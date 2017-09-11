package com.tokopedia.core;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginBypassModel;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.PasswordGenerator.PGListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.welcome.WelcomeActivity;

import org.parceler.Parcels;

/**
 * modified by m.normansyah
 * @since 3 Februari 2016
 *
 * fetch some data from server in order to worked around.
 */
public class SplashScreen extends AppCompatActivity implements DownloadResultReceiver.Receiver{
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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                finish();
                return;
            }
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash_screen);
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        sessionHandler = new SessionHandler(this);
        resetAllDatabaseFlag();
        initPermissionReactNativeDev();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new DiscoveryInteractorImpl().editProductDetail(this, "45469593", "", "");
        moveToHome();
    }

    private void moveToHome() {
//        new android.os.Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                Pgenerator = new PasswordGenerator(SplashScreen.this);
                InitNew();
                registerFCMDeviceID();
            }
//        }, TIME_DELAY);



	private void InitNew () {
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
                bypassV2Login();
            }
        };
    }

    private void registerFCMDeviceID() {
	    GCMHandler gcm = new GCMHandler(this);
        gcm.actionRegisterOrUpdateDevice(getGCMHandlerListener());
    }

    public void finishSplashScreen() {
        Intent intent;
        if(isSeller()){
//            if(!sessionHandler.getShopID().isEmpty() && !sessionHandler.getShopID().equals("0")) {
//                // Means it is a Seller
//                startActivity(new Intent(SplashScreen.this, SellerHomeActivity.class));
//            } else {
//                // Means it is buyer
//                if(!TextUtils.isEmpty(sessionHandler.getLoginID())) {
//                    intent = moveToCreateShop(this);
//                    startActivity(intent);
//                } else {
//                    intent = new Intent(SplashScreen.this, WelcomeActivity.class);
//                    startActivity(intent);
//                }
//            }
            intent = new Intent(SplashScreen.this, WelcomeActivity.class);
        }else {
            intent = HomeRouter.getHomeActivity(this);
        }
        startActivity(intent);
        finish();
    }

    private boolean isSeller(){
        return getApplication().getClass().getSimpleName().equals("SellerMainApplication");
    }

    private void bypassV2Login() {
        if (SessionHandler.isV2Login(MainApplication.getAppContext())
                && !SessionHandler.isV4Login(MainApplication.getAppContext())) {

            LoginBypassModel loginBypassModel = new LoginBypassModel();
            loginBypassModel.setUserID(SessionHandler.getLoginID(MainApplication.getAppContext()));
            loginBypassModel.setDeviceID(GCMHandler.getRegistrationId(MainApplication.getAppContext()));
            Bundle bundle = new Bundle();
            bundle.putParcelable(DownloadService.LOGIN_BYPASS_MODEL_KEY, Parcels.wrap(loginBypassModel));
            DownloadService.startDownload(this, mReceiver, bundle, DownloadService.LOGIN_BYPASS);
            Crashlytics.setUserIdentifier(SessionHandler.getLoginID(MainApplication.getAppContext()));
        } else {
            finishSplashScreen();
        }

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        CommonUtils.dumper(resultData);
        if (resultCode == DownloadService.STATUS_FINISHED) finishSplashScreen();
    }

    private void resetAllDatabaseFlag(){
        LocalCacheHandler flagDB = new LocalCacheHandler(this, "DATABASE_VERSION" + MainApplication.DATABASE_VERSION);
        if(!flagDB.getBoolean("reset_db_flag", false)){
            Log.i("DATABASE DATABSE UWOOO", "clearing the database flag");
            LocalCacheHandler.clearCache(this, CategoryDatabaseManager.KEY_STORAGE_NAME);
            LocalCacheHandler.clearCache(this, DataManagerImpl.SHIPPING_CITY_DURATION_STORAGE);
            if(getApplication() instanceof TkpdCoreRouter){
                ((TkpdCoreRouter)getApplication()).resetAddProductCache(this);
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


}
