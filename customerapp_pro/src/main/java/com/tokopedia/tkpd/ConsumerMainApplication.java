package com.tokopedia.tkpd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.intl.BuildConfig;
import com.tokopedia.intl.R;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.screenshot_observer.Screenshot;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

import java.util.HashMap;
import java.util.Map;

import kotlin.Pair;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends com.tokopedia.tkpd.app.ConsumerMainApplication
    implements Configuration.Provider{

    protected void setVersionName() {
        Pair<String, String> versions = AuthHelper.getVersionName(BuildConfig.VERSION_NAME);
        String version = versions.getFirst();
        String suffixVersion = versions.getSecond();

        if (!version.equalsIgnoreCase(AuthHelper.ERROR)) {
            GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
        } else {
            GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        }
        com.tokopedia.config.GlobalConfig.RAW_VERSION_NAME = BuildConfig.VERSION_NAME;// save raw version name
    }

    @NonNull
    @Override
    public String versionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public int versionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public void registerActivityLifecycleCallbacks() {
        super.registerActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(new Screenshot(getApplicationContext().getContentResolver(), new Screenshot.BottomSheetListener() {
            @Override
            public void onFeedbackClicked(Uri uri, String className, boolean isFromScreenshot) {
                openFeedbackForm(uri, className, isFromScreenshot);
            }
        }));
    }

    public void initConfigValues() {
        setVersionCode();
        setVersionName();

        GlobalConfig.APPLICATION_TYPE = 3;
        GlobalConfig.PACKAGE_APPLICATION = "com.tokopedia.intl";
        GlobalConfig.LAUNCHER_ICON_RES_ID = R.mipmap.ic_launcher_customerapp_pro;
        initFileDirConfig();

        GlobalConfig.DEBUG = BuildConfig.DEBUG;
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

    public String getOriginalPackageApp(){
        return new String(new char[]{
                99, 111, 109, 46, 116, 111, 107, 111, 112, 101,
                100, 105, 97, 46
        }) + new String(new char[]{
                105, 110, 116, 108
        });
    }

    protected void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        }
    }

    private void openFeedbackForm(Uri uri, String className, boolean isFromScreenshot) {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConst.FEEDBACK_FORM);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_URI_IMAGE", uri);
        intent.putExtra("EXTRA_IS_CLASS_NAME", className);
        intent.putExtra("EXTRA_IS_FROM_SCREENSHOT", isFromScreenshot);
        getApplicationContext().startActivity(intent);
    }

    @SuppressLint("RestrictedApi")
    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder().setInitializationExceptionHandler(throwable -> {
            Map<String, String> map = new HashMap<>();
            map.put("type", "init");
            map.put("error", Log.getStackTraceString(throwable));
            ServerLogger.log(Priority.P1, "WORK_MANAGER", map);
            throw new RuntimeException("WorkManager failed to initialize", throwable);
        }).build();
    }
}
