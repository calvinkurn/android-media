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

import com.crashlytics.android.Crashlytics;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.stetho.Stetho;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;
import com.localytics.android.Localytics;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCoreGeneratedDatabaseHolder;
import com.tkpd.library.TkpdMultiDexApplication;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.network.di.module.NetModule;
import com.tokopedia.core.react.ReactNativeHostFactory;
import com.tokopedia.core.service.HUDIntent;
import com.tokopedia.core.util.GlobalConfig;

import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Example application for adding an L1 image cache to Volley.
 *
 * @author Trey Robinson
 */
public class MainApplication extends TkpdMultiDexApplication implements ReactApplication{


	public static final int DATABASE_VERSION = 7;
    public static final int DEFAULT_APPLICATION_TYPE = -1;
    private static Context context;
	private static Activity activity;
	private static Boolean isResetNotification = false;
	private static Boolean isResetDrawer = false;
	private static Boolean isResetCart = false;
    private static Boolean isResetTickerState = true;
	private static int currActivityState;
	private static String currActivityName;
    private static IntentService RunningService;
    public static HUDIntent hudIntent;
    public static ServiceConnection hudConnection;
    public static String PACKAGE_NAME;
    public static MainApplication instance;
    private final ReactNativeHost reactNativeHost = ReactNativeHostFactory.init(this);

    private DaggerAppComponent.Builder daggerBuilder;

    public int getApplicationType(){
        return DEFAULT_APPLICATION_TYPE;
    }

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MainApplication.context = getApplicationContext();
        //Track.setDebugMode(true);
        //Feature.enableDebug(true);
        init();
//		initImageLoader();
        initFacebook();
        initCrashlytics();
        initializeAnalytics();
        initANRWatchDogs();
        initStetho();
        PACKAGE_NAME = getPackageName();
        isResetTickerState=true;

        //[START] this is for dev process
		initDB();

		initDbFlow();

        Localytics.autoIntegrate(this);

        daggerBuilder = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule());
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

    public static void setActivityState(int param) {
        currActivityState = param;
    }

    public static int getActivityState() {
        return currActivityState;
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

//	private void initImageLoader() {
//		File cacheDir = StorageUtils.getCacheDirectory(context);
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//				.discCache(new UnlimitedDiscCache(cacheDir)) // default
//				.memoryCache(new UsingFreqLimitedMemoryCache(20000))
//				.threadPoolSize(5)
//				.denyCacheImageMultipleSizesInMemory()
//				.build();// default
//		ImageLoader.getInstance().init(config);
//	}

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

    private void initializeAnalytics() {
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.GTM);
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.APPSFLYER);
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.LOCALYTICS);
        TrackingUtils.runFirstTime(TrackingUtils.AnalyticsKind.MOENGAGE);
        TrackingUtils.setMoEngageExistingUser();
        TrackingUtils.enableDebugging(isDebug());
    }


    public void initANRWatchDogs() {
        if (!BuildConfig.DEBUG) {
            ANRWatchDog watchDog = new ANRWatchDog();
            watchDog.setReportMainThreadOnly();
            watchDog.setANRListener(new ANRWatchDog.ANRListener() {
                @Override
                public void onAppNotResponding(ANRError error) {
                    Crashlytics.logException(error);
                }
            });
            watchDog.start();
        }
    }

    public void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setUserIdentifier("");
    }

    public static void setRunningService(IntentService service) {
        RunningService = service;
    }

    public static IntentService getRunningService() {
        return RunningService;
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

    public AppComponent getApplicationComponent(ActivityModule activityModule) {
        return daggerBuilder.activityModule(activityModule)
                .build();
    }

    public void initStetho() {
        if (GlobalConfig.isAllowDebuggingTools()) Stetho.initializeWithDefaults(context);
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return reactNativeHost;
    }
}