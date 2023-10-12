package com.tokopedia.notifications.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.messaging.FirebaseMessaging
import com.tokopedia.fcmcommon.service.SyncFcmTokenService


class PushTokenRefreshWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val TAG = "DeleteFcmTokenWorker"

    override suspend fun doWork(): Result {
        Log.d(TAG, "Push Deletion Worker started")
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { p0 ->
                Log.d(TAG, "Push Deletion Task success: " + p0.isSuccessful)
                Log.d(TAG, "Push Deletion Token deleted")
                retrieveNewToken()
            }
        return Result.success()
    }

    private fun retrieveNewToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { p0 ->
                Log.d(TAG, "Push Deletion Task success: " + p0.isSuccessful)
                SyncFcmTokenService.startService(applicationContext)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
