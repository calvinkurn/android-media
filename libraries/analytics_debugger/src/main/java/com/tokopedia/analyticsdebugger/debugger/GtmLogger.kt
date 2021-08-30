package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.cassava.AnalyticsMapParser
import com.tokopedia.analyticsdebugger.database.TkpdAnalyticsDatabase
import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmRepo
import com.tokopedia.analyticsdebugger.debugger.domain.model.AnalyticsLogData
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GtmLogger private constructor(
    private val context: Context,
    private val mapParser: AnalyticsMapParser,
    private val dbSource: GtmRepo,
) : AnalyticsLogger, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("gtm_logger") + CoroutineExceptionHandler { _, t ->
            Timber.e(t, "gtm_logger")
        }

    private val cache: LocalCacheHandler = LocalCacheHandler(context, ANALYTICS_DEBUGGER)

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false) ?: false

    override fun save(name: String, data: Map<String, Any>, @AnalyticsSource source: String) {
        launch {
            val logData = AnalyticsLogData(
                name = name,
                data = mapParser.parse(data),
                source = source
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
                if (GlobalConfig.isAllowDebuggingTools() == true) {
                    val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
                    instance = GtmLogger(context, AnalyticsMapParser(), GtmRepo(dao))
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

                override fun save(
                    name: String,
                    data: Map<String, Any>,
                    @AnalyticsSource source: String
                ) {

                }

                override fun saveError(errorData: String) {

                }

                override fun enableNotification(status: Boolean) {

                }
            }
        }
    }
}
