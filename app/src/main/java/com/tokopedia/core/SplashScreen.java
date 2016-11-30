package com.tokopedia.core;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.data.DataManagerImpl;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.CategoryDatabaseManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandler.GCMHandlerListener;
import com.tokopedia.core.myproduct.presenter.AddProductPresenterImpl;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginBypassModel;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.PasswordGenerator.PGListener;
import com.tokopedia.core.util.SessionHandler;

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
    private PasswordGenerator Pgenerator;
    DownloadResultReceiver mReceiver;
	String id = null;

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

        resetAllDatabaseFlag();
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
                getGCMid();
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

    private void getGCMid() {
	    GCMHandler gcm = new GCMHandler(this);
        gcm.commitGCMProcess(getGCMHandlerListener());
    }

    private void finishSplashScreen() {
        startActivity(HomeRouter.getHomeActivity(this));
        finish();
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
            LocalCacheHandler.clearCache(this, AddProductPresenterImpl.FETCH_DEP_CHILD);
            LocalCacheHandler.clearCache(this, AddProductPresenterImpl.FETCH_DEP_PARENT);
            LocalCacheHandler.clearCache(this, AddProductPresenterImpl.FETCH_ETALASE);
        }

        flagDB.putBoolean("reset_db_flag", true);
        flagDB.applyEditor();
    }


}
