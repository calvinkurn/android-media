package com.tokopedia.notifications.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import com.tokopedia.fcmcommon.service.SyncFcmTokenService


class PushTokenRefreshWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val TAG = "DeleteFcmTokenWorker"

    override suspend fun doWork(): Result {
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { p0 ->
                retrieveNewToken()
            }
        return Result.success()
    }

    private fun retrieveNewToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { p0 ->
                SyncFcmTokenService.startService(applicationContext)
            }
        } catch (_: Exception) { }
    }
}
