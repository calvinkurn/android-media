package com.tokopedia.core.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCoreGeneratedDatabaseHolder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core2.BuildConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.fingerprint.LocationUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.service.HUDIntent;
import com.tokopedia.core.util.BranchSdkUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.util.toolargetool.TooLargeTool;

import java.util.List;

import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;

public abstract class MainApplication extends MainRouterApplication{

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
    private LocationUtils locationUtils;
    private DaggerAppComponent.Builder daggerBuilder;
    private AppComponent appComponent;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
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
    @Deprecated
    public static void setCurrentActivity(Activity currentActivity) {
        activity = currentActivity;
        if (activity != null) {
            CommonUtils.dumper(activity.getClass().getName());
        }
    }

    /**
     * please use Broadcast Manager not store activity within MainApplication.
     */
    @Deprecated
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
        //CommonUtils.dumper("asdasas");
        MainApplication.context = getApplicationContext();
        init();
        initCrashlytics();
        initStetho();
        PACKAGE_NAME = getPackageName();
        isResetTickerState = true;

        //[START] this is for dev process

        initDbFlow();

        daggerBuilder = DaggerAppComponent.builder()
                .appModule(new AppModule(this));
        getApplicationComponent().inject(this);

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
        TooLargeTool.startLogging(this);

        // initialize the Branch object
        initBranch();
        initializeAnalytics();
        NotificationUtils.setNotificationChannel(this);
        upgradeSecurityProvider();
    }

    private void upgradeSecurityProvider() {
        try {
            ProviderInstaller.installIfNeededAsync(this, new ProviderInstaller.ProviderInstallListener() {
                @Override
                public void onProviderInstalled() {
                    // Do nothing
                }

                @Override
                public void onProviderInstallFailed(int i, Intent intent) {
                    // Do nothing
                }
            });
        } catch (Throwable t) {
            // Do nothing
        }
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
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerActivityLifecycleCallbacks(new ActivityFrameMetrics.Builder().build());
        }
    }

    protected void initializeAnalytics() {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = TagManager.getInstance(context);
            tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        TrackingUtils.runAppsFylerFirstTime(this);
        TrackingUtils.runMoengageFirstTime(this);
        TrackingUtils.enableDebugging(this, isDebug());
    }

    public void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            Crashlytics.setUserIdentifier("");
        }
    }

    protected void initDbFlow() {
        if (BuildConfig.DEBUG) {
            FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);
        }
        FlowManager.init(new FlowConfig.Builder(this)
                .addDatabaseHolder(TkpdCoreGeneratedDatabaseHolder.class)
                .build());
    }

    public AppComponent getApplicationComponent() {
        return getAppComponent();
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = daggerBuilder.build();
        }
        return appComponent;
    }

    public void setAppComponent(AppComponent appComponent) {
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

    private void initFirebase() {
        if (GlobalConfig.DEBUG) {
            FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
            builder.setApplicationId("1:692092518182:android:9bb64c665e7c68ee");
            builder.setApiKey("AIzaSyDan4qOIiANywQFOk-AG-WhRxsEMVqfcbg");
            FirebaseApp.initializeApp(this, builder.build());
        }
    }

    @Override
    public Intent getSellerHomeActivityReal(Context context) {
        return SellerAppRouter.getSellerHomeActivity(context);
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return SellerAppRouter.getAppNotificationReceiver();
    }

    @Override
    public Class<?> getInboxMessageActivityClass() {
        return InboxRouter.getInboxMessageActivityClass();
    }

    @Override
    public Class<?> getInboxResCenterActivityClassReal() {
        return InboxRouter.getInboxResCenterActivityClass();
    }

    @Override
    public Intent getActivitySellingTransactionShippingStatusReal(Context mContext) {
        return SellerRouter.getActivitySellingTransactionShippingStatus(mContext);
    }

    @Override
    public Class getSellingActivityClassReal() {
        return SellerRouter.getSellingActivityClass();
    }

    @Override
    public Intent getActivitySellingTransactionListReal(Context mContext) {
        return SellerRouter.getActivitySellingTransactionList(mContext);
    }
	
    @Override
    public Intent getInboxTalkCallingIntent(Context mContext){
        return null;
    }



}
