package com.tokopedia.developer_options.tracker

import InfluxInteractor
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
        CoroutineScope(CoroutineName("influxTracker") + SupervisorJob() +
            CoroutineExceptionHandler { coroutineContext, throwable ->
                Timber.e(throwable)
            })

    private val influx: InfluxInteractor by lazy {
        InfluxInteractor.Builder().measurement("devops_tracker").build()
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
