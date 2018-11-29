package com.tokopedia.iris

import android.content.Context
import androidx.work.*
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.SendDataWorker
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(val context: Context) : Iris {
    private val trackingRepository: TrackingRepository = TrackingRepository.getInstance(context)
    private val session: Session = IrisSession(context)

    override fun setService(config: Configuration) {
        GlobalScope.launch {
            setWorkManager(config)
        }
    }

    override fun resetService(config: Configuration) {
        GlobalScope.launch {
            WorkManager.getInstance().cancelAllWorkByTag(WORKER_SEND_DATA)
            setWorkManager(config)
        }
    }

    override fun saveEvent(map: Map<String, Any>) {
        GlobalScope.launch(context = Dispatchers.IO) {
            // convert map to json then save as string
            trackingRepository.saveEvent(JSONObject(map).toString(), session.getSessionId(), session.getUserId())
        }
    }

    override fun sendEvent(map: Map<String, Any>) {
        GlobalScope.launch(context = Dispatchers.IO) {
            trackingRepository.sendSingleEvent(JSONObject(map).toString(), session.getSessionId(), session.getUserId())
        }
    }

    override fun setUserId(userId: String) {
        session.setUserId(userId)
    }

    private fun setWorkManager(config: Configuration) {
        val data: Data = Data.Builder().putInt(MAX_ROW, config.maxRow).build()
        val irisConstraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = PeriodicWorkRequestBuilder<SendDataWorker>(config.intervals, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(WORKER_SEND_DATA)
                .setConstraints(irisConstraint)
                .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(WORKER_SEND_DATA, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}