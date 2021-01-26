package com.tokopedia.sellerappwidget.view.work

import android.content.Context
import androidx.work.*
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.executor.GetOrderExecutor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/11/20
 */

class GetOrderWorker(
        private val context: Context,
        workerParams: WorkerParameters
) : BaseAppWidgetWorker(context, workerParams) {

    companion object {
        private const val TAG_RUN_PERIODIC = "get_order_worker"
        private const val REPEAT_INTERVAL = 15L
        private var workRequest: OneTimeWorkRequest? = null

        fun runWorker(context: Context) {
            val cacheHandler = AppWidgetHelper.getCacheHandler(context)
            val userSession: UserSessionInterface = UserSession(context)
            val isOrderWidgetEnabled = cacheHandler.getBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, false)
            val isUserLoggedIn = userSession.isLoggedIn
            if (!(isOrderWidgetEnabled && isUserLoggedIn)) {
                OrderAppWidget.showNoLoginState(context)
                workRequest = null
                return
            }

            if (workRequest == null) {
                val constraints = Constraints.Builder()
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

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            GetOrderExecutor.run(context, OrderAppWidget.DEFAULT_ORDER_STATUS_ID)
            super.doWork()
        }
    }
}