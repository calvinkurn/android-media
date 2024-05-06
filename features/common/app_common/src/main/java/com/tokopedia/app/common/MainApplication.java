package com.tokopedia.app.common;

import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.gcm.utils.NotificationUtils;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.locationmanager.LocationDetectorHelper;
import com.tokopedia.locationmanager.RequestLocationType;
import com.tokopedia.logger.LogManager;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.unifycomponents.ImageUnify;
import com.tokopedia.unifycomponents.ImageUrlLoader;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import javax.net.ssl.SSLContext;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


public abstract class MainApplication extends CoreNetworkApplication {

    protected UserSession userSession;
    protected RemoteConfig remoteConfig;
    private String MAINAPP_ADDGAIDTO_BRANCH = "android_addgaid_to_branch";
    private final String ENABLE_ASYNC_CRASHLYTICS_USER_INFO = "android_async_crashlytics_user_info";
    private final String ENABLE_ASYNC_BRANCH_USER_INFO = "android_async_branch_user_info";

    protected void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(MainApplication.this);
        WeaveInterface remoteConfigWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                return fetchRemoteConfig();
            }
        };
        Weaver.Companion.executeWeaveCoRoutineNow(remoteConfigWeave);
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void initFileDirConfig() {
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

        registerActivityLifecycleCallbacks(new AppActivityLifecycleCallback());
    }

    private void createAndCallBgWork() {
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
    private Boolean executeInBackground() {
        ImageUnifyInit.setImageCallback();
        new LocationDetectorHelper(MainApplication.this).getLocation(null,
                LocationDetectorHelper.TYPE_DEFAULT_FROM_CLOUD,
                RequestLocationType.APPROXIMATE_OR_PRECISE);
        upgradeSecurityProvider();
        return true;
    }

    private boolean fetchRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(this);
        remoteConfig.fetch(getRemoteConfigListener());
        return true;
    }

    private RemoteConfig.Listener getRemoteConfigListener() {
        return new RemoteConfig.Listener() {
            @Override
            public void onComplete(RemoteConfig remoteConfig) {
                LogManager logManager = LogManager.instance;
                if (logManager != null) {
                    logManager.refreshConfig();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        };
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
    private Boolean initBranch() {
        LinkerManager.initLinkerManager(getApplicationContext());
        if (remoteConfig.getBoolean(MAINAPP_ADDGAIDTO_BRANCH, false)) {
            LinkerManager.getInstance().setGAClientId(TrackingUtils.getClientID(getApplicationContext()));
        }
        WeaveInterface branchUserIdentityWeave = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                if (userSession.isLoggedIn()) {
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

    protected void setupAppScreenMode() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isForceLightMode = checkForceLightMode(sharedPreferences);
        if (isForceLightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            return;
        }

        boolean isDarkMode = sharedPreferences.getBoolean(TkpdCache.Key.KEY_DARK_MODE, false);
        int defaultScreenMode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        int screenMode = sharedPreferences.getInt(TkpdCache.Key.KEY_DARK_MODE_CONFIG_SCREEN_MODE, defaultScreenMode);

        AppCompatDelegate.setDefaultNightMode(screenMode);
    }

    private boolean checkForceLightMode(SharedPreferences sharedPreferences) {
        if (GlobalConfig.isSellerApp()) {
            if (remoteConfig.getBoolean(RemoteConfigKey.FORCE_LIGHT_MODE_SELLER_APP, false)) {
                sharedPreferences.edit().putBoolean(TkpdCache.Key.KEY_DARK_MODE, false).apply();
                return true;
            }
        } else {
            if (remoteConfig.getBoolean(RemoteConfigKey.FORCE_LIGHT_MODE, false)) {
                sharedPreferences.edit().putBoolean(TkpdCache.Key.KEY_DARK_MODE, false).apply();
                return true;
            }
        }
        return false;
    }
}