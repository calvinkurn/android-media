package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.ApplinkLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.ApplinkLogModel
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.analyticsdebugger.debugger.ui.activity.ApplinkDebuggerActivity
import com.tokopedia.config.GlobalConfig

import rx.Subscriber
import rx.schedulers.Schedulers


class ApplinkLogger private constructor(private val context: Context) : ApplinkLoggerInterface {
    private val dbSource: ApplinkLogDBSource
    private val cache: LocalCacheHandler

    private var applink = ""
    private var traces = ""

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, false)!!

    init {
        this.dbSource = ApplinkLogDBSource(context)
        this.cache = LocalCacheHandler(context, APPLINK_DEBUGGER)
    }

    override fun startTrace(applink: String) {
        this.applink = applink
        traces = ""
    }

    override fun appendTrace(trace: String) {
        traces += trace + "\n\n"
    }

    override fun save() {

        if (TextUtils.isEmpty(applink)) {
            return
        }

        try {
            val applinkLogModel = ApplinkLogModel()
            applinkLogModel.applink = applink
            applinkLogModel.traces = traces

            dbSource.insertAll(applinkLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())

            if (cache.getBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, false)!!) {
                NotificationHelper.show(context, applinkLogModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        applink = ""
        traces = ""
    }

    override fun wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
    }

    override fun openActivity() {
        context.startActivity(ApplinkDebuggerActivity.newInstance(context))
    }

    override fun enableNotification(isEnabled: Boolean) {
        cache.putBoolean(IS_APPLINK_DEBUGGER_NOTIF_ENABLED, isEnabled)
        cache.applyEditor()
    }

    private fun defaultSubscriber(): Subscriber<in Boolean> {
        return object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(aBoolean: Boolean?) {
                // no-op
            }
        }
    }

    companion object {
        private val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"
        private val IS_APPLINK_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled"

        var instance: ApplinkLoggerInterface? = null
            private set

        @JvmStatic
        fun getInstance(context: Context): ApplinkLoggerInterface {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools()!!) {
                    instance = ApplinkLogger(context.applicationContext)
                } else {
                    instance = emptyInstance()
                }
            }
            return instance as ApplinkLoggerInterface
        }

        private fun emptyInstance(): ApplinkLoggerInterface {
            return object : ApplinkLoggerInterface {

                override val isNotificationEnabled: Boolean
                    get() = false

                override fun startTrace(applink: String) {

                }

                override fun appendTrace(trace: String) {

                }

                override fun save() {

                }

                override fun wipe() {

                }

                override fun openActivity() {

                }

                override fun enableNotification(status: Boolean) {

                }
            }
        }
    }
}
