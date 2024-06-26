package com.tokopedia.analytics.byteio

import android.content.Context
import com.bytedance.applog.AppLog
import com.bytedance.applog.InitConfig
import com.bytedance.applog.UriConfig
import com.bytedance.applog.compress.CompressManager
import com.bytedance.bdinstall.Env
import com.bytedance.bdinstall.InstallUrl
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.user.session.UserSession

internal fun initAppLog(context: Context) {
    val channel = if (GlobalConfig.isAllowDebuggingTools()) "local_test" else GlobalConfig.STORE_CHANNEL
    val config = InitConfig("573733", channel)
    config.setAppName("Tokopedia")

    config.setUriConfig(
        UriConfig.Builder().also<UriConfig.Builder> { builder ->
            builder.setInstallEnv(
                Env(
                    InstallUrl(
                        "https://log.byteoversea.com/service/2/device_register/",
                        "https://i.byteoversea.com/service/2/app_alert_check/"
                    ),
                    true, false
                )
            )
            // 自定义埋点上报域名
            builder.setSendUris(
                arrayOf(
                    "https://log.byteoversea.com/service/2/app_log/",
                    "https://log15.byteoversea.com/service/2/app_log/"
                )
            )
            // 自定义实时埋点上报域名
            builder.setRealUris(
                arrayOf(
                    "https://rtlog.byteoversea.com/service/2/app_log/",
                    "https://rtlog.tiktokv.com/service/2/app_log/",
                    "https://rtapplog.tiktokv.com/service/2/app_log/"
                )
            )
            // 自定义 logsettings 上报域名
            builder.setSettingUri("https://log.byteoversea.com/service/2/log_settings/")
        }.build()
    )

    // TTNET: https://doc.bytedance.net/docs/2577/3086/22837/
    config.setNetworkClient(AppLogNetworkClient())

    config.setAutoStart(true)
    config.setHandleLifeCycle(true)
    AppLog.setEncryptAndCompress(true)
    AppLog.init(context, config)
    AppLog.setEnableEventUserId(true)
    AppLog.setHeaderInfo("ban_odin", 1)
    initUserIdForAppLog(context)

    AppLog.setAdjustTerminate(true)
    CompressManager.setReportStatsEnabled(false)
    AppLog.setMonitorEnabled(false)
}

fun initUserIdForAppLog(context: Context) {
    AppLog.registerHeaderCustomCallback {
        val userIdByTokopedia = UserSession(context).userId
        val custom = it?.getJSONObject("custom")
        custom?.put("user_id", userIdByTokopedia)
    }
    AppLog.setBDAccountCallback { // <user_type, user_id>
        val userIdByTokopedia = UserSession(context).userId
        android.util.Pair(12, userIdByTokopedia.toLongOrZero())
    }
}
