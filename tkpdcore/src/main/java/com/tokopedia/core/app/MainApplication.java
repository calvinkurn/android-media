package com.tokopedia.core.app;

import android.os.Build;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.fingerprint.LocationUtils;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.CoreNetworkApplication;
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

import java.io.File;


public abstract class MainApplication extends CoreNetworkApplication {

    private LocationUtils locationUtils;
    private DaggerAppComponent.Builder daggerBuilder;
    private AppComponent appComponent;
    private UserSession userSession;
    protected RemoteConfig remoteConfig;
    private String MAINAPP_ADDGAIDTO_BRANCH = "android_addgaid_to_branch";
    private static final String ENABLE_ASYNC_REMOTECONFIG_MAINAPP_INIT = "android_async_remoteconfig_mainapp_init";
    private final String ENABLE_ASYNC_CRASHLYTICS_USER_INFO = "android_async_crashlytics_user_info";
    private final String ENABLE_ASYNC_BRANCH_USER_INFO = "android_async_branch_user_info";

    protected void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(MainApplication.this);
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void initFileDirConfig(){
        GlobalConfig.INTERNAL_CACHE_DIR = this.getCacheDir().getAbsolutePath();
        GlobalConfig.INTERNAL_FILE_DIR = this.getFilesDir().getAbsolutePath();
        File extCacheDir = this.getExternalCacheDir();
        if (extCacheDir == null) {
            GlobalConfig.EXTERNAL_CACHE_DIR = "";
        } else {
            GlobalConfig.EXTERNAL_CACHE_DIR = extCacheDir.getAbsolutePath();
        }
        File extFileDir = this.getExternalFilesDir(null);
        if (extFileDir == null) {
            GlobalConfig.EXTERNAL_FILE_DIR = "";
        } else {
            GlobalConfig.EXTERNAL_FILE_DIR = extFileDir.getAbsolutePath();
        }
    }

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userSession = new UserSession(this);
        initCrashlytics();

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
            WeaveInterface crashlyticsUserInfoWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                    crashlytics.setUserId(userSession.getUserId());
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
}