package com.tokopedia.developer_options.tracker

import android.content.Context
import com.tokopedia.skynet.IdType
import com.tokopedia.skynet.InfluxInteractor
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

object DevOpsTracker {

    private val scope =
        CoroutineScope(
            CoroutineName("influxTracker") + SupervisorJob() +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    Timber.e(throwable)
                }
        )

    private var influx: InfluxInteractor? = null

    fun init(context: Context) {
        if (influx == null) {
            try {
                val id = UserSession(context).androidId
                scope.launch(Dispatchers.IO) {
                    val i = InfluxInteractor.Builder("tkpd:tkpd")
                        .measurement("devops_tracker")
                        .setIdentity(IdType.CUSTOM(id))
                        .build()
                    if (i.ping()) {
                        influx = i
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun trackClickEvent(page: String) {
        if (influx == null) return
        scope.launch(Dispatchers.IO) {
            influx?.send(
                tags = mapOf("page" to page, "event" to "click"),
                values = mapOf("count" to 1)
            )
        }
    }
}
