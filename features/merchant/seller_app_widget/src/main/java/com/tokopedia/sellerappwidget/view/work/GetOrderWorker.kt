package com.tokopedia.sellerappwidget.view.work

import android.content.Context
import androidx.work.*
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.service.GetOrderService
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/11/20
 */

class GetOrderWorker(
        private val context: Context,
        workerParams: WorkerParameters
) : Worker(context, workerParams) {

    companion object {
        private const val TAG_RUN_PERIODIC = "get_order_worker"
        private const val REPEAT_INTERVAL = 15L
        private var workRequest: OneTimeWorkRequest? = null

        fun runWorker(context: Context) {
            if (workRequest == null) {
                val constraints = Constraints.Builder()
                        .setRequiresDeviceIdle(false)
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()

                workRequest = OneTimeWorkRequest.Builder(GetOrderWorker::class.java)
                        .setConstraints(constraints)
                        .setInitialDelay(REPEAT_INTERVAL, TimeUnit.MINUTES)
                        .build()
            }

            workRequest?.let {
                WorkManager.getInstance(context)
                        .beginUniqueWork(TAG_RUN_PERIODIC, ExistingWorkPolicy.REPLACE, it)
                        .enqueue()
            }
        }
    }

    override fun doWork(): Result {
        GetOrderService.startService(context, OrderAppWidget.DEFAULT_ORDER_STATUS_ID)
        return Result.success()
    }
}