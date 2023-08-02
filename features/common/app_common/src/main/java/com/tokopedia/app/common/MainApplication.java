package com.tokopedia.app.common;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.fingerprint.LocationUtils;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.CoreNetworkApplication;
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

import javax.net.ssl.SSLContext;


public abstract class MainApplication extends CoreNetworkApplication {

    protected UserSession userSession;
    protected RemoteConfig remoteConfig;
    private String MAINAPP_ADDGAIDTO_BRANCH = "android_addgaid_to_branch";
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

    @Override
    public void onCreate() {
        super.onCreate();
        userSession = new UserSession(this);
        initCrashlytics();
        initAnalyticUserId();

        initBranch();
        NotificationUtils.setNotificationChannel(this);
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
                RemoteConfigKey.ENABLE_SEQ3_ASYNC, context, true);
    }

    @NotNull
    private Boolean executeInBackground(){
        new LocationUtils(MainApplication.this).initLocationBackground();
        upgradeSecurityProvider();
        return true;
    }

    private void upgradeSecurityProvider() {
        try {
            ProviderInstaller.installIfNeeded(this);
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().showErrorNotification(this, e.getConnectionStatusCode());
        } catch (Throwable t) {
            // Do nothing
            t.printStackTrace();
        }
    }

    public void initCrashlytics() {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            WeaveInterface crashlyticsUserInfoWeave = new WeaveInterface() {
                @NotNull
                @Override
                public Object execute() {
                    FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                    crashlytics.setUserId(userSession.getUserId());
                    return true;
                }
            };
            Weaver.Companion.executeWeaveCoRoutineWithFirebase(crashlyticsUserInfoWeave, ENABLE_ASYNC_CRASHLYTICS_USER_INFO, getApplicationContext(), true);
        }
    }

    public void initAnalyticUserId() {
        WeaveInterface crashlyticsAnalyticsUserIdWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                String userId = userSession.getUserId();
                TkpdFirebaseAnalytics.getInstance(MainApplication.this).setUserId(userId);
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineNow(crashlyticsAnalyticsUserIdWeave);
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
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(branchUserIdentityWeave, ENABLE_ASYNC_BRANCH_USER_INFO, getApplicationContext(), true);
        return true;
    }
}