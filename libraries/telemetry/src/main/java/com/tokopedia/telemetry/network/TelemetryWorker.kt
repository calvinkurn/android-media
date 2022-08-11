package com.tokopedia.telemetry.network

import android.content.Context
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.telemetry.model.Telemetry
import com.tokopedia.telemetry.network.data.TelemetryResponse
import com.tokopedia.telemetry.network.di.DaggerTelemetryComponent
import com.tokopedia.telemetry.network.di.TelemetryComponent
import com.tokopedia.telemetry.network.di.TelemetryModule
import com.tokopedia.telemetry.network.usecase.TelemetryUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TelemetryWorker : CoroutineScope {

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

    fun doWork() {
        launch {
            try {
                isWorkerRunning = true
                if (!userSession.isLoggedIn) {
                    return@launch
                }
                // loop all telemetry section that has event start-end, send to server
                val telemetrySectionList = Telemetry.telemetrySectionList
                val telemetrySectionSize = telemetrySectionList.size
                if (telemetrySectionSize > 0) {
                    for (i in telemetrySectionSize - 1 downTo 0) {
                        val telemetrySection = telemetrySectionList.getOrNull(i) ?: continue
                        if (telemetrySection.eventNameEnd.isNotEmpty()) {
                            var telemetryResponse: TelemetryResponse? = null
                            try {
                                telemetryResponse = telemetryUseCase.execute(telemetrySection)
                            } catch (e: Exception) {
                                // no internet connection
                                if (e is UnknownHostException ||
                                    e is SocketException ||
                                    e is InterruptedIOException ||
                                    e is ConnectionShutdownException ||
                                    e is CancellationException
                                ) {
                                    // do send data later, break the loop.
                                    break
                                }
                            }
                            if (telemetryResponse?.isError() == true) {
                                sendLog(
                                    ERROR_TYPE_SEND_BACKEND,
                                    telemetryResponse.subDvcTl.data.errorMessage
                                )
                            } else {
                                telemetrySectionList.removeAt(i)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                sendLog(ERROR_TYPE_EXCEPTION, ex.toString())
            } finally {
                isWorkerRunning = false
            }
        }
    }

    companion object {
        const val LOG_TAG = "GQL_ERROR_RISK"
        const val LOG_ERROR_TYPE = "errorType"
        const val LOG_ERROR = "error"
        const val LOG_TYPE = "type"
        const val TELEMETRY = "telemetry"

        const val ERROR_TYPE_SEND_BACKEND = "error on send to backend"
        const val ERROR_TYPE_EXCEPTION = "error exception do work"

        var telemetryComponent: TelemetryComponent? = null
        var isWorkerRunning = false
        var telemetryWorker: TelemetryWorker? = null

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
            getWorker(context).doWork()
        }

        private fun getWorker(context: Context): TelemetryWorker {
            val tmp = telemetryWorker
            return if (tmp == null) {
                val obj = TelemetryWorker()
                inject(context.applicationContext, obj)
                obj
            } else {
                tmp
            }
        }

        fun inject(appContext: Context, telemetryWorker: TelemetryWorker) {
            if (telemetryComponent == null) {
                telemetryComponent = DaggerTelemetryComponent.builder()
                    .telemetryModule(TelemetryModule(appContext))
                    .build()
            }
            telemetryComponent?.inject(telemetryWorker)
        }
    }

}