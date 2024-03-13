package com.tokopedia.tkpd.utils

import android.app.Application
import android.content.Context
import android.util.Log
import com.bytedance.apm.Apm
import com.bytedance.apm.config.ActivityLeakDetectConfig
import com.bytedance.apm.config.ApmInitConfig
import com.bytedance.apm.config.ApmStartConfig
import com.bytedance.apm.core.IDynamicParams
import com.bytedance.apm.listener.IApmStartListener
import com.bytedance.apm.trace.TraceStats
import com.bytedance.applog.AppLog
import com.bytedance.bdinstall.Level
import com.bytedance.crash.ICommonParams
import com.bytedance.crash.Npth
import com.bytedance.crash.runtime.AllDefaultUrls

object SlardarInit {

    fun initNpth(application: Application) {
        Npth.setApplication(application)
        Npth.init(application, object : ICommonParams {
            override fun getCommonParams(): MutableMap<String, Any> {
                val result = mutableMapOf<String, Any>()
                val params = mutableMapOf<String, String>()
                AppLog.putCommonParams(AppLog.getContext(), params, true, Level.L0)
                result.putAll(params)
                return result
            }

            override fun getDeviceId(): String {
                return AppLog.getDid()
            }

            override fun getUserId(): Long {
                return 0 // todo
            }

            override fun getSessionId(): String {
                return AppLog.getSessionId()
            }

            override fun getPluginInfo(): MutableMap<String, Int>? {
                return null
            }

            override fun getPatchInfo(): MutableList<String>? {
                return null
            }

        }, true, true, true)

        Npth.getConfigManager().javaCrashUploadUrl = "https://slardar-bd-sg.feishu.cn/monitor/collect/c/crash"
        Npth.getConfigManager().setNativeCrashUrl("https://slardar-bd-sg.feishu.cn/monitor/collect/c/native_bin_crash")
        Npth.getConfigManager().setLaunchCrashUrl("https://slardar-bd-sg.feishu.cn/monitor/collect/c/exception/dump_collection")
        Npth.getConfigManager().setConfigGetUrl("https://slardar-bd-sg.feishu.cn/monitor/appmonitor/v3/settings")
        Npth.getConfigManager().alogUploadUrl = "https://slardar-bd-sg.feishu.cn/monitor/collect/c/logcollect"
    }

    /**
     * better invoke on Application#onCreate
     * UI Thread required, very fast
     */
    fun initApm(context: Context) {
        val initBuilder = ApmInitConfig.builder()
        // 缓存队列最大限制设置为1000
        initBuilder.cacheBufferCount(1000)
        // 启动阶段的网络数据打标记
        initBuilder.traceExtraFlag(TraceStats.FLAG_EXTRA_INFO_NET)
        // 启动阶段打标记时间限制是30秒
        initBuilder.traceExtraCollectTimeMs(30 * 1000.toLong())
        // 开启上报慢函数堆栈
        initBuilder.reportEvilMethodSwitch(true)
        // 慢函数阈值是1000毫秒
        initBuilder.evilMethodThresholdMs(1000)
        // Activity泄漏上报和兜底处理
        initBuilder.detectActivityLeak(ActivityLeakDetectConfig.builder()
            .gcDetectActivityLeak(false)
            .reportActivityLeakEvent(true)
            .waitDetectActivityTimeMs(100 * 1000.toLong())
            .unbindActivityLeakSwitch(true)
            .build())
        initBuilder.maxValidPageLoadTimeMs(20 * 1000.toLong())
        initBuilder.enableDeviceInfoOnPerfData(true)
        Apm.getInstance().init(context.applicationContext, initBuilder.build())
    }

    /**
     * can be invoke after app launched since it may cost time
     */
    fun startApm() {
        val builder = ApmStartConfig.builder()
//        val headerInfo: JSONObject = AppLog.getHeader() // todo better copy
        builder.blockDetectOnlySampled(true)
            .blockDetect(true)
            .setUnSampleListener { tag, message ->
                Log.d("LogDeva", "$tag")
            }
            .seriousBlockDetect(true)
            .delayReport(60)
            .configFetchUrl(arrayListOf(
                "https://slardar-bd-sg.feishu.cn/monitor/appmonitor/v2/settings",
                "https://i.isnssdk.com/monitor/appmonitor/v2/settings",
                "https://mon.isnssdk.com/monitor/appmonitor/v2/settings",
                "https://mon.tiktokv.com/monitor/appmonitor/v2/settings",
                "https://mon-va.tiktokv.com/monitor/appmonitor/v2/settings",
                "https://mon-sg.tiktokv.com/monitor/appmonitor/v2/settings"))
            .exceptionLogDefaultReportUrls(arrayListOf(
                "https://slardar-bd-sg.feishu.cn/monitor/collect/c/exception",
                "https://i.isnssdk.com/monitor/collect/c/exception",
                "https://mon.isnssdk.com/monitor/collect/c/exception",
                "https://mon-va.tiktokv.com/monitor/collect/c/exception"))
            .defaultReportUrls(arrayListOf(
                "https://slardar-bd-sg.feishu.cn/monitor/collect/",
                "https://i.isnssdk.com/monitor/collect/",
                "https://mon.isnssdk.com/monitor/collect/",
                "https://mon.tiktokv.com/monitor/collect/",
                "https://mon-va.tiktokv.com/monitor/collect/",
                "https://mon-sg.tiktokv.com/monitor/collect/"))
            .aid(573733)
            .deviceId(AppLog.getDid())
            .appVersion("100") // todo
            .updateVersionCode("10000") // todo
            .channel("local_test") // todo
            .memoryReachTop { type -> /*do sth to trim mem*/ }
            .apmStartListener(object : IApmStartListener {
                override fun onStartComplete() {}
                override fun onReady() {
                }
            })
        builder.batteryDetect(true)
        builder.useDefaultTTNetImpl(true)
        builder.dynamicParams(object : IDynamicParams {
            override fun getCommonParams(): Map<String, String> {
                return mapOf()
            }

            override fun getSessionId(): String {
                return AppLog.getSessionId()
            }

            override fun getUid(): Long {
                return 0 // todo return your real uid
            }
        })
        builder.queryParams {
            val params =
                HashMap<String, String>()
            params["app_id"] = "573733"
            params
        }
        builder.apmLogListener { logType, logSubType, log ->
            Log.d("LogDeva", "$logType")
        }
        Apm.getInstance().start(builder.build())

//        if (DebugConfig.isOpen()) {
//            ApmContext.setDebugMode(true)
//            SDKContext.setDebugMode(true)
//        }
    }
}
