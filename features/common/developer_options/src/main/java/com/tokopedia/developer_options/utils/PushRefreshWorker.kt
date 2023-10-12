package com.tokopedia.developer_options.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.tokopedia.fcmcommon.service.SyncFcmTokenService.Companion.startService


class PushRefreshWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    private val TAG = "DeleteFcmTokenWorker"

    override suspend fun doWork(): Result {
        Log.d(TAG, "Worker started")
        FirebaseMessaging.getInstance().deleteToken()
            .addOnCompleteListener { p0 ->
                Log.d(TAG, "Task success: " + p0.isSuccessful)
                Log.d(TAG, "Token deleted")
                retrieveNewToken()
            }
        return Result.success()
    }

    private fun retrieveNewToken() {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { p0 ->
                Log.d(TAG, "Task success: " + p0.isSuccessful)
                startService(applicationContext)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
