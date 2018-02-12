package com.tokopedia.core.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCoreGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.fingerprint.LocationUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.cache.domain.interactor.CacheApiWhiteListUseCase;
import com.tokopedia.core.cache.domain.model.CacheApiWhiteListDomain;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.service.HUDIntent;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.toolargetool.TooLargeTool;

import java.util.List;

import javax.inject.Inject;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import rx.Subscriber;

public abstract class MainApplication extends BaseMainApplication{

	public static final int DATABASE_VERSION = 7;
    public static final int DEFAULT_APPLICATION_TYPE = -1;
    private static final String TAG = "MainApplication";
    public static HUDIntent hudIntent;
    public static ServiceConnection hudConnection;
    public static String PACKAGE_NAME;
    public static MainApplication instance;
    private static Context context;
	private static Activity activity;
	private static Boolean isResetNotification = false;
	private static Boolean isResetDrawer = false;
	private static Boolean isResetCart = false;
    private static Boolean isResetTickerState = true;
	private static int currActivityState;
	private static String currActivityName;
    private static IntentService RunningService;
    @Inject
    CacheApiWhiteListUseCase cacheApiWhiteListUseCase;
    private LocationUtils locationUtils;
    private DaggerAppComponent.Builder daggerBuilder;
    private AppComponent appComponent;

    /**
     * Get list of white list
     *
     * @return
     */
    protected abstract List<CacheApiWhiteListDomain> getWhiteList();

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(MainApplication.this);
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public synchronized static Context getAppContext() {
        return MainApplication.context;
    }

    /**
     * please use Broadcast Manager not store activity within MainApplication.
     *
     * @param currentActivity
     */
    public static void setCurrentActivity(Activity currentActivity) {
        activity = currentActivity;
        if (activity != null) {
            CommonUtils.dumper(activity.getClass().getName());
        }
    }

    /**
     * please use Broadcast Manager not store activity within MainApplication.
     */
    public static Activity currentActivity() {
        return activity;
    }

    public static Boolean resetNotificationStatus(Boolean status) {
        isResetNotification = status;
        return isResetNotification;
    }

    public static Boolean resetDrawerStatus(Boolean status) {
        isResetDrawer = status;
        return isResetDrawer;
    }

    public static Boolean resetCartStatus(Boolean status) {
        isResetCart = status;
        return isResetCart;
    }

    public static Boolean getIsResetTickerState() {
        return isResetTickerState;
    }

    public static void setIsResetTickerState(Boolean isResetTickerState) {
        MainApplication.isResetTickerState = isResetTickerState;
    }

    public static Boolean getNotificationStatus() {
        return isResetNotification;
    }

    public static Boolean getDrawerStatus() {
        return isResetDrawer;
    }

    public static Boolean getCartStatus() {
        return isResetCart;
    }

    public static int getActivityState() {
        return currActivityState;
    }

    public static void setActivityState(int param) {
        currActivityState = param;
    }

    public static void setActivityname(String param) {
        currActivityName = param;
        if (HUDIntent.isRunning)
            hudIntent.printClassName(param);
    }

    public static String getActivityName() {
        return currActivityName;
    }

    public static Boolean isTablet() {
          /*return (context.getResources().getConfiguration().screenLayout
		            & Configuration.SCREENLAYOUT_SIZE_MASK)
		            >= Configuration.SCREENLAYOUT_SIZE_LARGE;*/
        return false;
    }

    public static int getCurrentVersion(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getOrientation(Activity context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isLandscape(Activity context) {
        return getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static boolean isLandscape(Context context) {
        return getOrientation(context) == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static void bindHudService() {
        HUDIntent.bindService(context, new HUDIntent.HUDInterface() {
            @Override
            public void onServiceConnected(HUDIntent service, ServiceConnection connection) {
                hudIntent = service;
                hudConnection = connection;
                hudIntent.printMessage("Binded on MainApplication");
            }

            @Override
            public void onServiceDisconnected() {

            }
        });
    }

    public static void unbindHudService() {
        hudIntent.printMessage("Unbinded from MainApplication");
        HUDIntent.unbindService(context, hudConnection);
    }

    public static IntentService getRunningService() {
        return RunningService;
    }

    public static void setRunningService(IntentService service) {
        RunningService = service;
    }

    public int getApplicationType() {
        return DEFAULT_APPLICATION_TYPE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MainApplication.context = getApplicationContext();
        init();
        initFacebook();
        initCrashlytics();
        initStetho();
        initializeAnalytics();
        PACKAGE_NAME = getPackageName();
        isResetTickerState = true;

        //[START] this is for dev process
        initDB();

        initDbFlow();

        daggerBuilder = DaggerAppComponent.builder()
                .appModule(new AppModule(this));
        getApplicationComponent().inject(this);

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
        TooLargeTool.startLogging(this);

        addToWhiteList();
        // initialize the Branch object
        initBranch();
        NotificationUtils.setNotificationChannel(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }



    public void addToWhiteList() {
        List<CacheApiWhiteListDomain> cacheApiWhiteListDomains = getWhiteList();
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(CacheApiWhiteListUseCase.ADD_WHITELIST_COLLECTIONS, cacheApiWhiteListDomains);
        cacheApiWhiteListUseCase.executeSync(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, e.toString());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Log.i(TAG, aBoolean.toString());
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        locationUtils.deInitLocationBackground();
    }

    /**
     * Intialize the request manager and the image cache
     */
    private void init() {
    }

    /**
     * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.
     */

    private void initFacebook() {

    }

    protected void initializeAnalytics() {
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.GTM);
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.APPSFLYER);
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.MOENGAGE);
        TrackingUtils.setMoEngageExistingUser();
        TrackingUtils.enableDebugging(isDebug());
    }

    public void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setUserIdentifier("");
    }

    public void initDB() {
    }

	private void initDbFlow() {
		if(BuildConfig.DEBUG) {
			FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
		}
		FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdCoreGeneratedDatabaseHolder.class)
                .build());
        //FlowManager.initModule(TkpdCoreGeneratedDatabaseHolder.class);
	}

    public AppComponent getApplicationComponent() {
        return getAppComponent();
    }

    public AppComponent getAppComponent(){
        if (appComponent == null) {
            appComponent = daggerBuilder.build();
        }
        return appComponent;
    }

    public void setAppComponent(AppComponent appComponent){
        this.appComponent = appComponent;
    }

    public void initStetho() {
        if (GlobalConfig.isAllowDebuggingTools()) Stetho.initializeWithDefaults(context);
    }

    private void initBranch() {
        Branch.getAutoInstance(this);
        if (SessionHandler.isV4Login(this)) {
            BranchSdkUtils.sendIdentityEvent(SessionHandler.getLoginID(this));
        }
    }
}