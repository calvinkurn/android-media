package com.tokopedia.notifications.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tokopedia.notifications.data.AmplificationDataSource
import com.tokopedia.notifications.utils.PushAmplificationRefreshUtil.Companion.application

class PushAmplificationWorker (appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val TAG = "PushAmplificationWorker"
    override suspend fun doWork(): Result {
        Log.d(TAG, "Push Amplification Worker started")
        AmplificationDataSource.invoke(application)
        return Result.success()
    }
}
