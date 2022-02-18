package com.tokopedia.tkpd;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.content.Intent;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.preference.PreferenceManager;

import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.analytics.performance.util.AppStartPerformanceTracker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.screenshot_observer.Screenshot;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

import io.embrace.android.embracesdk.Embrace;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends com.tokopedia.tkpd.app.ConsumerMainApplication {

    private static final String SUFFIX_ALPHA = "-alpha";

    @Override
    public void initConfigValues() {
        GlobalConfig.PACKAGE_APPLICATION = "com.tokopedia.tkpd";
        GlobalConfig.LAUNCHER_ICON_RES_ID = com.tokopedia.tkpd.R.mipmap.ic_launcher_customerapp;
        setVersionCode();
        setVersionName();
        initFileDirConfig();

        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.IS_PREINSTALL = BuildConfig.IS_PREINSTALL;
        com.tokopedia.config.GlobalConfig.PREINSTALL_NAME = BuildConfig.PREINSTALL_NAME;
        com.tokopedia.config.GlobalConfig.PREINSTALL_DESC = BuildConfig.PREINSTALL_DESC;
        com.tokopedia.config.GlobalConfig.PREINSTALL_SITE = BuildConfig.PREINSTALL_SITE;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.ENABLE_DEBUG_TRACE = BuildConfig.ENABLE_DEBUG_TRACE;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = MainParentActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeeplinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        if (com.tokopedia.config.GlobalConfig.DEBUG) {
            com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceInfo.getAndroidId(this);
        }
        if (BuildConfig.DEBUG_TRACE_NAME != null) {
            com.tokopedia.config.GlobalConfig.DEBUG_TRACE_NAME = BuildConfig.DEBUG_TRACE_NAME.split(",");
        }
    }

    public String getOriginalPackageApp() {
        return new String(new char[]{
                99, 111, 109, 46, 116, 111, 107, 111, 112, 101,
                100, 105, 97, 46
        }) + new String(new char[]{
                116, 107, 112, 100
        });
    }

    @Override
    public String versionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public int versionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public void onCreate() {
        CheckAndTraceAppStartIfEnabled();
        Embrace.getInstance().start(this);
        super.onCreate();
        setupAppScreenMode();
        setupAlphaObserver();

    }

    public void CheckAndTraceAppStartIfEnabled() {
        if (BuildConfig.ENABLE_DEBUG_TRACE && BuildConfig.DEBUG_TRACE_NAME.contains(AppStartPerformanceTracker.APP_START_COLD)) {
            initConfigValues();
            AppStartPerformanceTracker.startMonitoring();

            //track first launch of activity
            ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                public void connectListener() {
                    AppStartPerformanceTracker.stopMonitoring();
                    ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
                }
            });
        }
    }

    private boolean checkForceLightMode() {
        if (remoteConfig.getBoolean(RemoteConfigKey.FORCE_LIGHT_MODE, false)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putBoolean(TkpdCache.Key.KEY_DARK_MODE, false).apply();
            return true;
        }
        return false;
    }

    private void setupAppScreenMode() {
        boolean isForceLightMode = checkForceLightMode();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = sharedPreferences.getBoolean(TkpdCache.Key.KEY_DARK_MODE, false);
        int screenMode;
        if (isDarkMode && !isForceLightMode) {
            screenMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            screenMode = AppCompatDelegate.MODE_NIGHT_NO;
        }
        AppCompatDelegate.setDefaultNightMode(screenMode);
    }

    private void openFeedbackForm(Uri uri, String className, boolean isFromScreenshot) {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConst.FEEDBACK_FORM);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_URI_IMAGE", uri);
        intent.putExtra("EXTRA_IS_CLASS_NAME", className);
        intent.putExtra("EXTRA_IS_FROM_SCREENSHOT", isFromScreenshot);
        getApplicationContext().startActivity(intent);
    }

    private void setupAlphaObserver() {
        String versionName = BuildConfig.VERSION_NAME;
        if (versionName.endsWith(SUFFIX_ALPHA) && remoteConfig.getBoolean(RemoteConfigKey.ENABLE_APLHA_OBSERVER, true)) {
            registerActivityLifecycleCallbacks(new AlphaObserver());
            registerActivityLifecycleCallbacks(new Screenshot(getApplicationContext().getContentResolver(), new Screenshot.BottomSheetListener() {
                @Override
                public void onFeedbackClicked(Uri uri, String className, boolean isFromScreenshot) {
                    openFeedbackForm(uri, className, isFromScreenshot);
                }
            }));
        }
    }
}
