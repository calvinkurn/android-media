package com.tokopedia.iris.worker

import android.content.Context
import android.content.Intent
import androidx.core.app.BaseJobIntentService
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.util.Cache
import com.tokopedia.iris.util.DEFAULT_MAX_ROW
import com.tokopedia.iris.util.JOB_IRIS_ID
import com.tokopedia.iris.util.MAX_ROW
import com.tokopedia.iris.worker.IrisExecutor.handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by meta on 24/05/19.
 */
class IrisService : BaseJobIntentService(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        IrisExecutor.executor + handler
    }

    companion object {
        var isRunning = false
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, IrisService::class.java, JOB_IRIS_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            val maxRow = intent.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
            startService(maxRow)
        } catch (e: java.lang.Exception) {
            Timber.e("P2#IRIS#onHandleWork %s", e.toString())
        }
    }

    fun startService(maxRow: Int) {
        launch(coroutineContext) {
            try {
                if (isRunning) {
                    return@launch
                }
                isRunning = true
                val cache = Cache(applicationContext)
                if (cache.isEnabled()) {
                    val trackingRepository = TrackingRepository(applicationContext)
                    val dataSize = trackingRepository.sendRemainingEvent(maxRow)
                    if (dataSize == 0) {
                        IrisAnalytics.getInstance(applicationContext).setAlarm(false)
                    }
                }
            } catch (e: Exception) {
                Timber.e("P2#IRIS#startService %s", e.toString())
            } finally {
                isRunning = false
            }
        }
    }
}

