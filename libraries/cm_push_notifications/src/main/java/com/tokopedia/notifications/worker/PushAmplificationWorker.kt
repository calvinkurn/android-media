package com.tokopedia.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tokopedia.notifications.data.AmplificationDataSource
import com.tokopedia.notifications.utils.PushAmplificationRefreshUtil.Companion.application

class PushAmplificationWorker (appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val TAG = "PushAmplificationWorker"
    override suspend fun doWork(): Result {
        try {
            application?.let { AmplificationDataSource.invoke(it) }
            return Result.success()
        } catch (_: Exception) { return Result.failure() }
    }
}
