package com.tokopedia.sellerappwidget.view.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 07/12/20
 */

abstract class BaseAppWidgetWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                pruneWorkIfPossible()
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun pruneWorkIfPossible() {
        val sharedPref = SellerAppWidgetPreferences.getInstance(context)
        val nowMillis = System.currentTimeMillis()
        val oneHourMillis = TimeUnit.HOURS.toMillis(1)
        val lastPrune = sharedPref.getLong(Const.SharedPrefKey.LAST_PRUNE_WORK, System.currentTimeMillis())
        if (lastPrune <= nowMillis.minus(oneHourMillis)) {
            WorkManager.getInstance(context).pruneWork()
            sharedPref.putLong(Const.SharedPrefKey.LAST_PRUNE_WORK, nowMillis)
        }
    }
}