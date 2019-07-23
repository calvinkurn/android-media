package com.tokopedia.iris.worker

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log
import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.JOB_IRIS_ID
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.worker.IrisExecutor.handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by meta on 24/05/19.
 */
class IrisService : JobIntentService(), CoroutineScope {

    private lateinit var mContext: Context

    override val coroutineContext: CoroutineContext by lazy {
        IrisExecutor.executor + handler
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, IrisService::class.java, JOB_IRIS_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            val maxRow = intent.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
            startService(maxRow)
        } catch (e: java.lang.Exception) {
        }
    }

    private fun startService(maxRow: Int) {
        launch(coroutineContext + Dispatchers.IO) {
            try {
                val trackingRepository = TrackingRepository(applicationContext)
                trackingRepository.sendRemainingEvent(maxRow)
            } catch (e: Exception) {
                Log.d("IRIS startService", e.message)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

