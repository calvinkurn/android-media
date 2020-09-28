package com.tokopedia.core.app;

import android.content.Context;
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
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core2.BuildConfig;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import io.fabric.sdk.android.Fabric;

public abstract class MainApplication extends MainRouterApplication{

    public static final int DATABASE_VERSION = 7;
    public static String PACKAGE_NAME;
    public static MainApplication instance;
    private LocationUtils locationUtils;
    private DaggerAppComponent.Builder daggerBuilder;
    private AppComponent appComponent;
    private UserSession userSession;
    protected RemoteConfig remoteConfig;
    private String MAINAPP_ADDGAIDTO_BRANCH = "android_addgaid_to_branch";
    private static final String ENABLE_ASYNC_REMOTECONFIG_MAINAPP_INIT = "android_async_remoteconfig_mainapp_init";
    private final String ENABLE_ASYNC_CRASHLYTICS_USER_INFO = "android_async_crashlytics_user_info";
    private final String ENABLE_ASYNC_BRANCH_USER_INFO = "android_async_branch_user_info";


    public static MainApplication getInstance() {
        return instance;
    }

    protected void initRemoteConfig() {
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return remoteConfig = new FirebaseRemoteConfigImpl(MainApplication.this);
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(remoteConfigWeave, ENABLE_ASYNC_REMOTECONFIG_MAINAPP_INIT, MainApplication.this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MainApplication.this);
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
        userSession = new UserSession(this);
        initCrashlytics();
        PACKAGE_NAME = getPackageName();

        daggerBuilder = DaggerAppComponent.builder()
                .appModule(new AppModule(this));
        getApplicationComponent().inject(this);

        initBranch();
        NotificationUtils.setNotificationChannel(this);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            upgradeSecurityProvider();
        }
        createAndCallBgWork();
    }

    private void createAndCallBgWork(){
        WeaveInterface executeBgWorkWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                return executeInBackground();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(executeBgWorkWeave,
                RemoteConfigKey.ENABLE_SEQ3_ASYNC, context);
    }

    @NotNull
    private Boolean executeInBackground(){
        locationUtils = new LocationUtils(MainApplication.this);
        locationUtils.initLocationBackground();
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            upgradeSecurityProvider();
        }
        return true;
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
        if(locationUtils != null) {
            locationUtils.deInitLocationBackground();
        }
    }

    public void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            WeaveInterface crashlyticsUserInfoWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    Crashlytics.setUserIdentifier(userSession.getUserId());
                    Crashlytics.setUserEmail(userSession.getEmail());
                    Crashlytics.setUserName(userSession.getName());
                    return true;
                }
            };
            Weaver.Companion.executeWeaveCoRoutineWithFirebase(crashlyticsUserInfoWeave, ENABLE_ASYNC_CRASHLYTICS_USER_INFO, getApplicationContext());
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

    //this method needs to be called from here in case of migration get it tested from CM team
    @NotNull
    private Boolean initBranch(){
        LinkerManager.initLinkerManager(getApplicationContext());
        if(remoteConfig.getBoolean(MAINAPP_ADDGAIDTO_BRANCH, false)){
            LinkerManager.getInstance().setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
        }
        WeaveInterface branchUserIdentityWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                if(userSession.isLoggedIn()) {
                    UserData userData = new UserData();
                    userData.setUserId(userSession.getUserId());
                    LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY,
                            userData));
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(branchUserIdentityWeave, ENABLE_ASYNC_BRANCH_USER_INFO, getApplicationContext());
        return true;
    }

    @Override
    public Class<?> getInboxMessageActivityClass() {
        return InboxRouter.getInboxMessageActivityClass();
    }

    @Override
    public Class<?> getInboxResCenterActivityClassReal() {
        return InboxRouter.getInboxResCenterActivityClass();
    }
}