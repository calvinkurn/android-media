package com.tokopedia.telemetry.network

import android.content.Context
import androidx.work.*
import com.tokopedia.telemetry.model.Telemetry
import com.tokopedia.telemetry.network.di.DaggerTelemetryComponent
import com.tokopedia.telemetry.network.di.TelemetryModule
import com.tokopedia.telemetry.network.usecase.TelemetryUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TelemetryWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var telemetryUseCase: TelemetryUseCase

    init {
        DaggerTelemetryComponent.builder()
                .telemetryModule(TelemetryModule(appContext))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            isWorkerRunning = true
            try {
                val userSession = getUserSession(appContext)
                if (!userSession.isLoggedIn) {
                    Result.success()
                }
                // loop all telemetry section that has event start-end, send to server
                val telemetrySectionList = Telemetry.telemetrySectionList
                val telemetrySectionSize = telemetrySectionList.size
                if (telemetrySectionSize > 0) {
                    for (i in telemetrySectionSize - 1 downTo 0) {
                        val telemetrySection = telemetrySectionList[i]
                        if (telemetrySection.eventNameEnd.isNotEmpty()) {
                            telemetryUseCase.execute(telemetrySection)
                            telemetrySectionList.removeAt(i)
                        }
                    }
                }
            } catch (e: Exception) {

            }
            isWorkerRunning = false
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "TelemetryWorker"
        var isWorkerRunning: Boolean = false

        var userSession: UserSessionInterface? = null

        private fun createNewWorker(): OneTimeWorkRequest {
            // we do not use periodic because it can only run every 15 minutes
            return OneTimeWorkRequest
                .Builder(TelemetryWorker::class.java)
                .setConstraints(Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build())
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()
        }
        fun scheduleWorker(context: Context) {
            GlobalScope.launch {
                try {
                    val userSession = getUserSession(context)
                    if (!userSession.isLoggedIn) {
                        return@launch
                    }
                    WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        if (isWorkerRunning) {
                            ExistingWorkPolicy.APPEND
                        } else {
                            ExistingWorkPolicy.REPLACE
                        },
                        createNewWorker()
                    )
                } catch (ex: Exception) {
                    Timber.w(ex.toString())
                }
            }
        }

        fun getUserSession(context: Context): UserSessionInterface {
            if (userSession == null) {
                userSession = UserSession(context.applicationContext)
            }
            return userSession!!
        }

    }

}