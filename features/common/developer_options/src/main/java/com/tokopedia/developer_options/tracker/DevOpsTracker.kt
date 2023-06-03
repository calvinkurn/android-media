package com.tokopedia.developer_options.tracker

import IdType
import InfluxInteractor
import android.content.Context
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

object DevOpsTracker {

    private val scope =
        CoroutineScope(
            CoroutineName("influxTracker") + SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    Timber.e(throwable)
                }
        )

    private lateinit var influx: InfluxInteractor

    fun init(context: Context) {
        if (!::influx.isInitialized) {
            val id = UserSession(context).androidId
            influx = InfluxInteractor.Builder()
                .measurement("devops_tracker")
                .setIdentity(IdType.CUSTOM(id))
                .build()
        }
    }

    fun trackImpression(page: String) {
        scope.launch {
            withContext(Dispatchers.IO) {
                influx.send(
                    tags = mapOf("page" to page, "event" to "click"),
                    values = mapOf("count" to 1)
                )
            }
        }
    }
}
