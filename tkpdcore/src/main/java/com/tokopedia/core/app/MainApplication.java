package com.tokopedia.core.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.gms.security.ProviderInstaller;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TkpdCoreGeneratedDatabaseHolder;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.fingerprint.LocationUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.service.HUDIntent;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.toolargetool.TooLargeTool;
import com.tokopedia.core2.BuildConfig;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.user.session.UserSession;

import io.fabric.sdk.android.Fabric;

public abstract class MainApplication extends MainRouterApplication{

    public static final int DATABASE_VERSION = 7;
    private static final String TAG = "MainApplication";
    public static HUDIntent hudIntent;
    public static ServiceConnection hudConnection;
    public static String PACKAGE_NAME;
    public static MainApplication instance;
    private static Boolean isResetNotification = false;
    private static Boolean isResetCart = false;
    private static Boolean isResetTickerState = true;
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

    public static Boolean resetNotificationStatus(Boolean status) {
        isResetNotification = status;
        return isResetNotification;
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

    public static Boolean getCartStatus() {
        return isResetCart;
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

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initCrashlytics();
        initStetho();
        PACKAGE_NAME = getPackageName();
        isResetTickerState = true;

        initDbFlow();

        daggerBuilder = DaggerAppComponent.builder()
                .appModule(new AppModule(this));
        getApplicationComponent().inject(this);

        locationUtils = new LocationUtils(this);
        locationUtils.initLocationBackground();
        TooLargeTool.startLogging(this);

        initBranch();
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

    private void init() {
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerActivityLifecycleCallbacks(new ActivityFrameMetrics.Builder().build());
        }
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
        LinkerManager.initLinkerManager(getApplicationContext());

        UserSession userSession = new UserSession(this);

        if(userSession.isLoggedIn()) {
            UserData userData = new UserData();
            userData.setUserId(userSession.getUserId());

            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY,
                    userData));
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
