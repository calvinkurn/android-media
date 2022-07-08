package com.tokopedia.telemetry.network

import android.content.Context
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.telemetry.model.Telemetry
import com.tokopedia.telemetry.network.di.DaggerTelemetryComponent
import com.tokopedia.telemetry.network.di.TelemetryComponent
import com.tokopedia.telemetry.network.di.TelemetryModule
import com.tokopedia.telemetry.network.usecase.TelemetryUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

object TelemetryWorker : CoroutineScope {

    const val LOG_TAG = "GQL_ERROR_RISK"
    const val LOG_ERROR_TYPE = "errorType"
    const val LOG_ERROR = "error"
    const val LOG_TYPE = "type"
    const val TELEMETRY = "telemetry"

    const val ERROR_TYPE_SEND_BACKEND = "error on send to backend"

    var telemetryComponent: TelemetryComponent? = null
    var isWorkerRunning = false

    @Inject
    lateinit var telemetryUseCase: TelemetryUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    private val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, _ -> }
    }

    // allowing only 1 thread at a time
    override val coroutineContext: CoroutineContext by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + handler
    }

    suspend fun doWork() {
        // loop all telemetry section that has event start-end, send to server
        val telemetrySectionList = Telemetry.telemetrySectionList
        val telemetrySectionSize = telemetrySectionList.size
        if (telemetrySectionSize > 0) {
            for (i in telemetrySectionSize - 1 downTo 0) {
                val telemetrySection = telemetrySectionList[i]
                if (telemetrySection.eventNameEnd.isNotEmpty()) {
                    val result = telemetryUseCase.execute(telemetrySection)
                    if (result.isError()) {
                        sendLog(ERROR_TYPE_SEND_BACKEND, result.subDvcTl.data.errorMessage)
                    }
                    telemetrySectionList.removeAt(i)
                }
            }
        }
    }

    private fun sendLog(errorType: String, error: String) {
        ServerLogger.log(
            Priority.P1,
            LOG_TAG,
            mapOf(
                LOG_TYPE to TELEMETRY,
                LOG_ERROR_TYPE to errorType,
                LOG_ERROR to error,
            )
        )
    }

    fun scheduleWorker(context: Context) {
        if (isWorkerRunning) {
            return
        }
        TelemetryWorker.launch {
            try {
                isWorkerRunning = true
                inject(context)
                if (!userSession.isLoggedIn) {
                    return@launch
                }
                doWork()
            } catch (ex: Exception) {
                Timber.w(ex.toString())
            } finally {
                isWorkerRunning = false
            }
        }
    }

    fun inject(appContext: Context) {
        if (telemetryComponent == null) {
            telemetryComponent = DaggerTelemetryComponent.builder()
                .telemetryModule(TelemetryModule(appContext))
                .build()
        }
        if (!::userSession.isInitialized) {
            telemetryComponent?.inject(TelemetryWorker)
        }
    }

}