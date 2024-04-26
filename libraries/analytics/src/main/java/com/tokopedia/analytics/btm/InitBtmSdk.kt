package com.tokopedia.analytics.btm

import android.app.Application
import android.content.Context
import android.util.Log
import com.bytedance.android.btm.api.BtmSDK
import com.bytedance.android.btm.api.depend.IAppLogDepend
import com.bytedance.android.btm.api.depend.ILogDepend
import com.bytedance.android.btm.api.depend.ISettingDepend
import com.bytedance.android.btm.api.depend.OnSettingUpdateCallback
import com.bytedance.android.btm.api.model.BtmSDKBuilder
import com.bytedance.android.btm.api.model.EventModelV1
import com.bytedance.android.btm.api.model.EventModelV3
import com.bytedance.applog.AppLog
import timber.log.Timber

object InitBtmSdk {

    fun init(context: Context) {
        BtmSDK.init(BtmSDKBuilder().apply {
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
                    Log.d("BtmDev event", modelV3.event + " -> " + modelV3.params)
                    model.event?.let {
                        AppLog.onEventV3(it, model.params)
                    }
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
                    //TODO  JC notice, deploy firebase settings for btm
                    return ""
                }

                override fun registerUpdateCallback(callback: OnSettingUpdateCallback) {

                }
            }
            defaultA = "a87943"
            deviceId = AppLog.getDid()
            enableDebugCrash = false
            enableBtmPageAnnotation = false
            appId = Integer.getInteger(AppLog.getAppId()) ?: 0
            //TODO TOKO does not have thread pool management and uses the default thread pool
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
            //TODO When pulled up by other applications, we need to pay attention to the name of the pulled up application.
//            appLaunchDepend = object : IAppLaunchDepend {
//                override fun getReferrer(): String {
//                    return ServiceManager.get().getService(IRouteMonitorApi::class.java)
//                        .getReferrer()
//                }
//            }
        })
        BtmSDK.getDepend().addUnknownWhiteClass(
            "com.ss.android.ugc.aweme.splash.SplashActivity"
        )
    }

}
