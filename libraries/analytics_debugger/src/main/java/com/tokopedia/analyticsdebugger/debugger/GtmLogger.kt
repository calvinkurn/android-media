package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.text.TextUtils
import com.tokopedia.analyticsdebugger.AnalyticsSource
import com.tokopedia.analyticsdebugger.cassava.AnalyticsMapParser
import com.tokopedia.analyticsdebugger.cassava.data.CassavaSharedPreference
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
    private val pref: CassavaSharedPreference,
) : AnalyticsLogger, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = CoroutineName("gtm_logger") + CoroutineExceptionHandler { _, t ->
            Timber.e(t, "gtm_logger")
        }

    override fun save(data: Map<String, Any>, name: String?, @AnalyticsSource source: String) {
        launch {
            val nameNotNull = name ?: mapParser.inferName(data, source)
            val logData = AnalyticsLogData(
                name = nameNotNull,
                data = mapParser.parse(data),
                source = source
            )
            if (!TextUtils.isEmpty(logData.name) && logData.name != "null") {
                dbSource.insert(logData)
            } else {
                Timber.w("analytics data was not logged because of empty name")
            }

            if (pref.isNotifEnabled()) {
                NotificationHelper.show(context, logData)
            }
        }

    }

    override fun enableNotification(status: Boolean) {
        pref.setNotifEnabled(status)
    }

    override fun isNotificationEnabled(): Boolean {
        return pref.isNotifEnabled()
    }

    companion object {

        private var instance: AnalyticsLogger? = null

        @JvmStatic
        fun getInstance(context: Context): AnalyticsLogger {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools() == true) {
                    val dao = TkpdAnalyticsDatabase.getInstance(context).gtmLogDao()
                    instance = GtmLogger(
                        context,
                        AnalyticsMapParser(),
                        GtmRepo(dao),
                        CassavaSharedPreference(context)
                    )
                } else {
                    instance = emptyInstance()
                }
            }

            return instance as AnalyticsLogger
        }

        private fun emptyInstance(): AnalyticsLogger {
            return object : AnalyticsLogger {

                override fun save(
                    data: Map<String, Any>,
                    name: String?,
                    @AnalyticsSource source: String
                ) {

                }

                override fun enableNotification(status: Boolean) {

                }

                override fun isNotificationEnabled(): Boolean {
                    return false
                }
            }
        }
    }
}
