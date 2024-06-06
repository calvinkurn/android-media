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
import com.tokopedia.config.GlobalConfig
import org.json.JSONObject
import timber.log.Timber

object InitBtmSdk {
    private const val TAG = "InitBtmSDK"
    private const val SPLASH_SCREEN = "com.tokopedia.tkpd.ConsumerSplashScreen"

    fun init(context: Context) {
        val isBtmDisabled = !BtmLibraFeatureFlag.isBtmEnabled()
        if (isBtmDisabled) return

        Timber.tag(TAG).i("-------init-------")
        BtmSDK.init(
            BtmSDKBuilder().apply {
                app = context.applicationContext as Application
                appIds = arrayOf(AppLog.getAppId())
                debug = GlobalConfig.isAllowDebuggingTools()
                versionName = GlobalConfig.VERSION_NAME
                updateVersionCode = GlobalConfig.VERSION_CODE.toString()
                appLogDepend = object : IAppLogDepend {
                    override fun onEventV1(model: EventModelV1) {
                    }

                    override fun onEventV3(model: EventModelV3) {
                        if (model.event == null) {
                            return
                        }
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
                defaultA = Tokopedia.code
                deviceId = AppLog.getDid()
                enableDebugCrash = false
                enableBtmPageAnnotation = false
                appId = Integer.getInteger(AppLog.getAppId()) ?: 0
            }
        )
        BtmSDK.getDepend().addUnknownWhiteClass(SPLASH_SCREEN)
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
