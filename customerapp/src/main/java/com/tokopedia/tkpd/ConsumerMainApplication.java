package com.tokopedia.tkpd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.bytedance.applog.AppLog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.preference.PreferenceManager;
import androidx.work.Configuration;

import com.bytedance.bdinstall.BDInstall;
import com.bytedance.mobsec.metasec.ov.MSConfig;
import com.bytedance.mobsec.metasec.ov.MSManager;
import com.bytedance.mobsec.metasec.ov.MSManagerUtils;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.analytics.performance.util.AppStartPerformanceTracker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.RollenceKey;
import com.tokopedia.remoteconfig.abtest.AbTestPlatform;
import com.tokopedia.screenshot_observer.Screenshot;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.analytics.performance.fpi.FrameMetricsMonitoring;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends com.tokopedia.tkpd.app.ConsumerMainApplication
        implements Configuration.Provider {

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
        GlobalConfig.ENABLE_MACROBENCHMARK_UTIL = BuildConfig.ENABLE_MACROBENCHMARK_UTIL;
        GlobalConfig.IS_NAKAMA_VERSION = BuildConfig.IS_NAKAMA_VERSION;
        GlobalConfig.STORE_CHANNEL = BuildConfig.STORE_CHANNEL;
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
        super.onCreate();
        super.setupAppScreenMode();
        initMsSDK();
        setupAlphaObserver();
        registerAppLifecycleCallbacks();

    }

    private void initMsSDK(){

        // prevent initiate mssdk as it will crash debug
        if(GlobalConfig.DEBUG)
            return;

        // 构建config的Builder对象
        String appID = "573733";

        // license需要在风控接入平台生成，license错误会触发crash 云安全平台配置安全SDK License/鉴权文件
        String licenseStr = this.getString(com.tokopedia.keys.R.string.mssdk_key);

        // 海外初始化参考，比如是Tiktok的使用COLLECT_MODE_TIKTOK_INIT完成初始化，海外使用国内的mode 会触发crash，表现为 "MSConfig init error!"
        MSConfig.Builder builder = new MSConfig.Builder(appID, licenseStr, MSConfig.COLLECT_MODE_TIKTOK_INIT);

        String deviceID = AppLog.getDid();
        String sessionID = AppLog.getSessionId();
        String installID = AppLog.getIid();

        // 配置config参数，通过builder生成config对象. 如果暂时获取不到，可以通过延迟配置，见下面说明
        builder.setDeviceID(deviceID);                                                    // 必填项，如果当前获取不到，可以在后续配置,如果设置空串会触发crash。注意⚠️：applog在新增设备上刚开始的返回值为空串，注册完成后需要重新设置
        builder.setClientType(MSConfig.CLIENT_TYPE_INHOUSE);          // 必填项，CLIENT_TYPE_INHOUSE代表字节内的应用，注意不要填写错误
        builder.setChannel(GlobalConfig.isAllowDebuggingTools() ? "local_test" : GlobalConfig.STORE_CHANNEL);                                                  // 必填项，有助于业务作弊分析，反爬虫使用
        builder.setInstallID(installID);                  // 必填项，如果初始化时候获取不到，可以不调用该接口，后续请业务通过MSManager 传入

        // 对于海外版，必填项。MSConfig.OVREGION_TYPE_SG代表新加坡，MSConfig.OVREGION_TYPE_VA代表美东。
        builder.setOVRegionType(MSConfig.OVREGION_TYPE_SG);

        // 构建MSConfig配置对象
        MSConfig config = builder.build();

        // 初始化SDK, 需要在Application的onCretae中进行初始化调用
        MSManagerUtils.init(this, config);

        BDInstall.addOnDataObserver( new com.bytedance.bdinstall.IDataObserver(){

             @Override
             public void onIdLoaded(String did, String iid, String ssid) {

             }

             @Override
             public void onRemoteIdGet(boolean changed, String oldDid, String newDid, String oldIid, String newIid, String oldSsid, String newSsid){
                if(changed){
                    // 如果当前无法获取deviceID和installID，可以在SDK初始化之后适当时间配置，方式如下所示;同时需要监听deviceID、installID的变更，及时传递给mssdk,并调用report接口，见注意事项
                    // 具体可以参考 AppLog接入文档 第5条 获取设备相关信息一节
                    MSManager mgr = MSManagerUtils.get(appID);
                    mgr.setDeviceID(newIid);
                    mgr.setInstallID(newDid);
                    mgr.report("did-iid-update");
                }
             }
         });

        MSManager mgr = MSManagerUtils.get(appID);

        mgr.setCollectMode(MSConfig.COLLECT_MODE_TIKTOK_NONUSEA);
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

    private void openFeedbackForm(Uri uri, String className, boolean isFromScreenshot) {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConst.FEEDBACK_FORM);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_URI_IMAGE", uri);
        intent.putExtra("EXTRA_IS_CLASS_NAME", className);
        intent.putExtra("EXTRA_IS_FROM_SCREENSHOT", isFromScreenshot);
        getApplicationContext().startActivity(intent);
    }

    private void setupAlphaObserver() {
        if (isAlphaVersion()) {
            registerActivityLifecycleCallbacks(new AlphaObserver());
        }
        if (isAlphaVersion() || isNakamaVersion()) {
            registerActivityLifecycleCallbacks(
                    new Screenshot(getApplicationContext().getContentResolver(), this::openFeedbackForm)
            );
        }
    }

    private boolean isAlphaVersion() {
        String versionName = GlobalConfig.VERSION_NAME;
        return versionName.endsWith(SUFFIX_ALPHA)
                && remoteConfig.getBoolean(RemoteConfigKey.ENABLE_APLHA_OBSERVER, true);
    }

    private boolean isNakamaVersion() {
        return GlobalConfig.IS_NAKAMA_VERSION;
    }

    @Nullable
    private AbTestPlatform getAbTestPlatform() {
        try {
            return RemoteConfigInstance.getInstance().getABTestPlatform();
        } catch (java.lang.IllegalStateException e) {
            return null;
        }
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
        }).build();
    }

    private void registerAppLifecycleCallbacks() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            registerActivityLifecycleCallbacks(new FrameMetricsMonitoring(this));
        }
    }
}
