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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Created by meta on 24/05/19.
 */
class IrisService : BaseJobIntentService(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.IO + CoroutineExceptionHandler { _, ex ->
            isRunning = false
            Timber.e("P1#IRIS#CoroutineExceptionIrisService %s", ex.toString())
        }
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
            Timber.e("P1#IRIS#onHandleWork %s", e.toString())
        }
    }

    fun startService(maxRow: Int) {
        if (isRunning) {
            return
        }
        launch(coroutineContext) {
            if (isRunning) {
                return@launch
            }
            try {
                isRunning = true
                IrisServiceCore.run(applicationContext, maxRow)
            } catch (e: Exception) {
                Timber.e("P1#IRIS#startService %s", e.toString())
            } finally {
                isRunning = false
            }
        }
    }
}

