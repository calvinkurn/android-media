package com.tokopedia.analytics.btm

import android.app.Application
import android.content.Context
import com.bytedance.android.btm.api.BtmSDK
import com.bytedance.android.btm.api.depend.IALogDepend
import com.bytedance.android.btm.api.depend.IAppLogDepend
import com.bytedance.android.btm.api.depend.ILogDepend
import com.bytedance.android.btm.api.depend.ISettingDepend
import com.bytedance.android.btm.api.depend.OnSettingUpdateCallback
import com.bytedance.android.btm.api.model.BtmSDKBuilder
import com.bytedance.android.btm.api.model.EventModelV1
import com.bytedance.android.btm.api.model.EventModelV3
import com.bytedance.applog.AppLog
import com.bytedance.applog.IEventObserver
import com.bytedance.dataplatform.remote_config.Experiments
import org.json.JSONObject
import timber.log.Timber

object InitBtmSdk {
    private const val TAG = "InitBtmSDK"

    fun init(context: Context) {
        Timber.tag(TAG).i("-------init-------")
        BtmSDK.init(
            BtmSDKBuilder().apply {
                app = context.applicationContext as Application
                appIds = arrayOf(AppLog.getAppId())
                debug = false
                versionName = "1"
                updateVersionCode = "1"
                appLogDepend = object : IAppLogDepend {
                    override fun onEventV1(model: EventModelV1) {
                    }

                    override fun onEventV3(model: EventModelV3) {
                        if (model.event == null) {
                            return
                        }
                        var modelV3 = EventModelV3(model.event, model.params)
                        modelV3 = BtmSDK.addBtmEventParam(modelV3)
                        Log.d(TAG, modelV3.event + " -> " + modelV3.params)
                        model.event?.let {
                            AppLog.onEventV3(it, model.params)
                        }
                    }
                }
                aLogDepend = object : IALogDepend {
                    override fun v(tag: String, msg: String) {
                        Timber.tag(tag).v(msg)
                    }

                    override fun i(tag: String, msg: String) {
                        Timber.tag(tag).i(msg)
                    }

                    override fun d(tag: String, msg: String) {
                        Timber.tag(tag).d(msg)
                    }

                    override fun w(tag: String, msg: String) {
                        Timber.tag(tag).w(msg)
                    }

                    override fun e(tag: String, msg: String) {
                        Timber.tag(tag).e(msg)
                    }
                }
                logDepend = object : ILogDepend {
                    override fun v(tag: String, msg: String) {
                        Timber.tag(tag).v(msg)
                    }

                    override fun i(tag: String, msg: String) {
                        Timber.tag(tag).i(msg)
                    }

                    override fun d(tag: String, msg: String) {
                        Timber.tag(tag).d(msg)
                    }

                    override fun w(tag: String, msg: String) {
                        Timber.tag(tag).w(msg)
                    }

                    override fun e(tag: String, msg: String) {
                        Timber.tag(tag).e(msg)
                    }
                }

                settingDepend = object : ISettingDepend {
                    override fun getSetting(): String? {
                        return Experiments.getBtmSettingString(false)
                    }

                    override fun registerUpdateCallback(callback: OnSettingUpdateCallback) {
                    }
                }
                defaultA = "a87943"
                deviceId = AppLog.getDid()
                enableDebugCrash = false
                enableBtmPageAnnotation = false
                appId = Integer.getInteger(AppLog.getAppId()) ?: 0
                // TODO TOKO does not have thread pool management and uses the default thread pool
//            executorDepend = object : IExecutorDepend {
//                override fun getCPUExecutor(): ExecutorService? {
//                    return ThreadPoolHelper.getDefaultExecutor()
//                }
//
//                override fun getIOExecutor(): ExecutorService? {
//                    return ThreadPoolHelper.getIOExecutor()
//                }
//
//                override fun getScheduledExecutor(): ScheduledExecutorService? {
//                    return ThreadPoolHelper.getScheduledExecutor()
//                }
//
//                override fun getSerialExecutor(): ExecutorService? {
//                    return ThreadPoolHelper.getSerialExecutor()
//                }
//            }
                // TODO When pulled up by other applications, we need to pay attention to the name of the pulled up application.
//            appLaunchDepend = object : IAppLaunchDepend {
//                override fun getReferrer(): String {
//                    return ServiceManager.get().getService(IRouteMonitorApi::class.java)
//                        .getReferrer()
//                }
//            }
            }
        )
        BtmSDK.getDepend().addUnknownWhiteClass(
            "com.tokopedia.tkpd.ConsumerSplashScreen"
        )
        AppLog.addEventObserver(appLogEventListener)
    }

    private val appLogEventListener = object : IEventObserver {
        override fun onEvent(
            category: String,
            tag: String,
            label: String?,
            value: Long,
            extValue: Long,
            extJson: String?
        ) {
            val modelV1 = EventModelV1(
                null, category, tag, label, value, extValue, true, JSONObject(extJson ?: ""), true
            )
            BtmSDK.addBtmEventParam(modelV1)
        }

        override fun onEventV3(event: String, params: JSONObject?) {
            BtmSDK.addBtmEventParam(EventModelV3(event, params))
        }

        override fun onMiscEvent(logType: String, params: JSONObject?) {
        }
    }
}

// const val Settings = """
//    {
//    "bugfix": {
//        "fix_bcm_description_big_size": 0,
//        "fix_fe_register_resume": 2,
//        "fix_register_page_when_no_resume": 0
//    },
//    "feature": {
//        "aLog": 1,
//        "add_token_in_chain": 1,
//        "bcm_check_switch": True,
//        "check_event_switch": 1,
//        "enable_bcm_report": 1,
//        "fe_switch": True,
//        "remove_enter_page": 1,
//        "show_id_chain_switch": 2,
//        "unknown_dialog_frag_switch": 0
//    },
//    "optimize": {
//        "enable_btm_page_show_opt": 1,
//        "enable_event_btm_map": 1
//    },
//    "sdk_switch": 1
// }
// """
