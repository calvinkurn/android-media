package com.tokopedia.journeydebugger

import android.content.Context
import android.text.TextUtils

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.journeydebugger.data.source.JourneyLogDBSource
import com.tokopedia.journeydebugger.domain.model.JourneyLogModel
import com.tokopedia.journeydebugger.helper.NotificationHelper
import com.tokopedia.journeydebugger.ui.activity.JourneyDebuggerActivity

import rx.Subscriber
import rx.schedulers.Schedulers


class JourneyLogger private constructor(private val context: Context) : JourneyLoggerInterface {
    private val dbSource: JourneyLogDBSource
    private val cache: LocalCacheHandler

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_JOURNEY_DEBUGGER_NOTIF_ENABLED, false)!!

    init {
        this.dbSource = JourneyLogDBSource(context)
        this.cache = LocalCacheHandler(context, JOURNEY_DEBUGGER)
    }

    override fun save(journey: String) {

        if (TextUtils.isEmpty(journey)) {
            return
        }

        try {
            val journeyLogModel = JourneyLogModel()
            journeyLogModel.journey = journey

            dbSource.insertAll(journeyLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())

            if (cache.getBoolean(IS_JOURNEY_DEBUGGER_NOTIF_ENABLED, false)!!) {
                NotificationHelper.show(context, journeyLogModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
    }

    override fun openActivity() {
        context.startActivity(JourneyDebuggerActivity.newInstance(context))
    }

    override fun enableNotification(isEnabled: Boolean) {
        cache.putBoolean(IS_JOURNEY_DEBUGGER_NOTIF_ENABLED, isEnabled)
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
        private val JOURNEY_DEBUGGER = "JOURNEY_DEBUGGER"
        private val IS_JOURNEY_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled"

        var instance: JourneyLoggerInterface? = null
            private set

        @JvmStatic
        fun getInstance(context: Context): JourneyLoggerInterface {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools()!!) {
                    instance = JourneyLogger(context.applicationContext)
                } else {
                    instance = emptyInstance()
                }
            }
            return instance as JourneyLoggerInterface
        }

        private fun emptyInstance(): JourneyLoggerInterface {
            return object : JourneyLoggerInterface {

                override val isNotificationEnabled: Boolean
                    get() = false

                override fun save(journey: String) {

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
