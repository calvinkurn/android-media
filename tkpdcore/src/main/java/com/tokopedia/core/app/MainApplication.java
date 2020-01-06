package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import androidx.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
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

    public static Boolean resetCartStatus(Boolean status) {
        isResetCart = status;
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

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initCrashlytics();
        PACKAGE_NAME = getPackageName();
        isResetTickerState = true;

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
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().showErrorNotification(this, e.getConnectionStatusCode());
        } catch (Throwable t) {
            // Do nothing
            t.printStackTrace();
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

    private void initBranch() {
        LinkerManager.initLinkerManager(getApplicationContext()).setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
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