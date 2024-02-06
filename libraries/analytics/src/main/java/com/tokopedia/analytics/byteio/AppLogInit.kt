package com.tokopedia.analytics.byteio

import android.content.Context
import com.bytedance.applog.AppLog
import com.bytedance.applog.InitConfig
import com.bytedance.applog.UriConfig
import com.bytedance.applog.compress.CompressManager
import com.bytedance.bdinstall.Env
import com.bytedance.bdinstall.InstallUrl

fun initAppLog(context: Context) {
    val config = InitConfig("573733", "local_test");
    config.setAppName("Tokopedia");

    config.setUriConfig(UriConfig.Builder().also<UriConfig.Builder> { builder ->
        builder.setInstallEnv(Env(InstallUrl(
            "https://log-va.tiktokv.com/service/2/device_register/",
            "https://log-va.tiktokv.com/service/2/app_alert_check/",
        ), true, false))
        // 自定义埋点上报域名
        builder.setSendUris(
            arrayOf(
                "https://log.tiktokv.com/service/2/app_log/",
                "https://applog.tiktokv.com/service/2/app_log/",
            ),
        )
        // 自定义实时埋点上报域名
        builder.setRealUris(
            arrayOf(
                "https://rtlog.tiktokv.com/service/2/app_log/",
                "https://rtapplog.tiktokv.com/service/2/app_log/",
            ),
        )
        // 自定义 logsettings 上报域名
        builder.setSettingUri("https://log.tiktokv.com/service/2/log_settings/")
    }.build())

// 注入自定义网络接口实现（可选），接入示例见下一节
// TTNET 接入 https://doc.bytedance.net/docs/2577/3086/22837/
    config.setNetworkClient(AppLogNetworkClient());

    config.setAutoStart(true);
    config.setHandleLifeCycle(true);
    AppLog.setEncryptAndCompress(true);
    AppLog.init(context, config);

    AppLog.setAdjustTerminate(true);
    CompressManager.setReportStatsEnabled(false);
    AppLog.setMonitorEnabled(false)
}
