package com.tokopedia.sellerappwidget.view.work

import android.content.Context
import androidx.work.*
import com.tokopedia.sellerappwidget.view.service.GetChatService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class GetChatWorker(
        private val context: Context,
        workerParams: WorkerParameters
) : BaseAppWidgetWorker(context, workerParams) {

    companion object {
        private const val TAG_WORKER = "get_chat_worker"
        private const val REPEAT_INTERVAL = 900L
        private var workRequest: OneTimeWorkRequest? = null

        @JvmStatic
        fun runWorkerPeriodically(context: Context) {
            if (workRequest == null) {
                val constraints = Constraints.Builder()
                        .setRequiresDeviceIdle(false)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                workRequest = OneTimeWorkRequestBuilder<GetChatWorker>()
                        .setConstraints(constraints)
                        .setInitialDelay(REPEAT_INTERVAL, TimeUnit.SECONDS)
                        .build()
            }

            workRequest?.let {
                WorkManager.getInstance(context)
                        .beginUniqueWork(TAG_WORKER, ExistingWorkPolicy.REPLACE, it)
                        .enqueue()
            }
        }
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            GetChatService.startService(context)
            super.doWork()
        }
    }
}