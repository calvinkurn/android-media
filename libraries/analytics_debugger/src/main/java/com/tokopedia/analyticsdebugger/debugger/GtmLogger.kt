package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDao
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.URLDecoder
import kotlin.coroutines.CoroutineContext

@Suppress("BlockingMethodInNonBlockingContext")
class GtmLogger private constructor(private val context: Context) : AnalyticsLogger, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("gtm_logger") + CoroutineExceptionHandler { _, t ->
            Timber.e(t, "gtm_logger")
        }

    private val gson: Gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
    private val dbSource by lazy {
        val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
        GtmRepo(dao)
    }
    private val cache: LocalCacheHandler = LocalCacheHandler(context, ANALYTICS_DEBUGGER)

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false) ?: false

    override fun save(name: String, data: Map<String, Any>, @AnalyticsSource source: String) {
        launch {
            val logData = AnalyticsLogData(
                    source = source,
                    name = name,
                    data = URLDecoder.decode(gson.toJson(data)
                            .replace("%(?![0-9a-fA-F]{2})".toRegex(), "%25")
                            .replace("\\+".toRegex(), "%2B"), "UTF-8")
            )
            if (!TextUtils.isEmpty(logData.name) && logData.name != "null") {
                dbSource.insert(logData)
            }

            if (isNotificationEnabled) {
                NotificationHelper.show(context, logData)
            }
        }

    }

    override fun saveError(errorData: String) {
        launch {
            val logData = AnalyticsLogData(
                    name = "ERROR GTM V5",
                    data = errorData,
                    source = AnalyticsSource.ERROR
            )
            dbSource.insert(logData)
            if (cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false)!!) {
                NotificationHelper.show(context, logData)
            }
        }
    }

    override fun enableNotification(status: Boolean) {
        cache.putBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, status)
        cache.applyEditor()
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
