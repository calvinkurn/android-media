package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.config.GlobalConfig
import rx.Subscriber
import rx.schedulers.Schedulers
import java.net.URLDecoder

class GtmLogger private constructor(private val context: Context) : AnalyticsLogger {

    private val gson: Gson by lazy { GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create() }
    private val dbSource: GtmLogDBSource = GtmLogDBSource(context)
    private val cache: LocalCacheHandler = LocalCacheHandler(context, ANALYTICS_DEBUGGER)

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false) ?: false

    override fun save(name: String, data: Map<String, Any>, @AnalyticsSource source: String) {
        try {
            val logData = AnalyticsLogData(
                    source = source,
                    name = name,
                    data = URLDecoder.decode(gson.toJson(data)
                            .replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                            .replace("\\+".toRegex(), "%2B"), "UTF-8")
            )
            if (!TextUtils.isEmpty(logData.name) && logData.name != "null") {
                dbSource.insertAll(logData)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(defaultSubscriber())
            }

            if (isNotificationEnabled) {
                NotificationHelper.show(context, logData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun saveError(errorData: String) {
        val data = AnalyticsLogData(
                name = "ERROR GTM V5",
                data = errorData,
                source = AnalyticsSource.ERROR
        )
        if (cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false)!!) {
            NotificationHelper.show(context, data)
        }
        dbSource.insertAll(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(defaultSubscriber())
    }

    override fun enableNotification(status: Boolean) {
        cache.putBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, status)
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
        private val ANALYTICS_DEBUGGER = "ANALYTICS_DEBUGGER"
        private val IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled"

        private var instance: AnalyticsLogger? = null

        @JvmStatic
        fun getInstance(context: Context): AnalyticsLogger {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools()!!) {
                    instance = GtmLogger(context)
                } else {
                    instance = emptyInstance()
                }
            }

            return instance as AnalyticsLogger
        }

        private fun emptyInstance(): AnalyticsLogger {
            return object : AnalyticsLogger {

                override val isNotificationEnabled: Boolean
                    get() = false

                override fun save(name: String, data: Map<String, Any>, @AnalyticsSource source: String) {

                }

                override fun saveError(errorData: String) {

                }

                override fun enableNotification(status: Boolean) {

                }
            }
        }
    }
}
