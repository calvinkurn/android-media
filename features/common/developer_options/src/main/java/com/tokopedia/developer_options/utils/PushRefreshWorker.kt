package com.tokopedia.developer_options.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.tokopedia.developer_options.presentation.service.DeleteFirebaseTokenService


class PushRefreshWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val intent = Intent(this.applicationContext, DeleteFirebaseTokenService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.applicationContext.startForegroundService(intent)
        } else {
            this.applicationContext.startService(intent)
        }
        return Result.success()
    }
}
