package com.tokopedia.notifications.worker

import android.content.Context
import androidx.work.*
import com.tokopedia.notifications.CMPushNotificationManager
import com.tokopedia.notifications.database.pushRuleEngine.PushRepository
import com.tokopedia.notifications.model.NotificationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

const val PERIODIC_TIME_INTERVAL_MINUTE = 16L
const val PUSH_WORKER_UNIQUE_NAME = "PUSH_WORKER"

class PushWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val TAG: String = PushWorker::class.java.simpleName

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                postOfflineNotification()
                deleteActiveNotification()
                deleteNotificationByStatus(NotificationStatus.DELETE)
                deleteAllPrevCompleted()
            } catch (e: Exception) {
            }
            Result.success()
        }
    }

    private suspend fun deleteAllPrevCompleted() {
        PushRepository.getInstance(appContext).pushDataStore
                .deleteNotification(System.currentTimeMillis(), NotificationStatus.COMPLETED)
    }

    private suspend fun deleteNotificationByStatus(status: NotificationStatus) {
        val baseNotificationModelList = PushRepository.getInstance(appContext)
                .pushDataStore
                .getNotificationByStatusList(status)
        baseNotificationModelList?.forEach { baseNotificationModel ->
            CMPushNotificationManager.instance.cancelOfflineNotification(baseNotificationModel = baseNotificationModel)
            baseNotificationModel.status = NotificationStatus.COMPLETED
            PushRepository.getInstance(appContext).updateNotificationModel(baseNotificationModel)
        }
    }

    private suspend fun deleteActiveNotification() {
        var baseNotificationModelList = PushRepository.getInstance(appContext)
                .pushDataStore
                .getNotificationByStatusList(NotificationStatus.ACTIVE)
        baseNotificationModelList?.forEach { baseNotificationModel ->
            if (baseNotificationModel.endTime <= System.currentTimeMillis()) {
                CMPushNotificationManager.instance.cancelOfflineNotification(baseNotificationModel = baseNotificationModel)
                baseNotificationModel.status = NotificationStatus.COMPLETED
                PushRepository.getInstance(appContext).updateNotificationModel(baseNotificationModel)
            }
        }
    }

    private suspend fun postOfflineNotification() {
        val baseNotificationModelList = PushRepository.getInstance(appContext).pushDataStore
                .getPendingNotificationList(System.currentTimeMillis())
        baseNotificationModelList?.forEach { baseNotificationModel ->
            CMPushNotificationManager.instance.postOfflineNotification(baseNotificationModel = baseNotificationModel)
            baseNotificationModel.status = NotificationStatus.ACTIVE
            PushRepository.getInstance(appContext).updateNotificationModel(baseNotificationModel)
        }
    }

    companion object{
        fun schedulePeriodicWorker(){
            val pushWorker = PeriodicWorkRequest
                    .Builder(PushWorker::class.java, PERIODIC_TIME_INTERVAL_MINUTE, TimeUnit.MINUTES)
                    .setConstraints(Constraints.NONE)
                    .build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                    PUSH_WORKER_UNIQUE_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    pushWorker)
        }
    }


}