package com.tokopedia.iris

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.SendDataWorker
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch


/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(context: Context) : Iris {
    private val trackingRepository: TrackingRepository = TrackingRepository(context)
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
            val event = JSONObject(map).toString()
            val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
            trackingRepository.saveEvent(resultEvent.toString(), session)
        }
    }

    override fun sendEvent(map: Map<String, Any>) {
        GlobalScope.launch(context = Dispatchers.IO) {
            trackingRepository.sendSingleEvent(JSONObject(map).toString(), session)
        }
    }

    override fun setUserId(userId: String) {
        session.setUserId(userId)
    }

    override fun setDeviceId(deviceId: String) {
        session.setDeviceId(deviceId)
    }

    private fun setWorkManager(config: Configuration) {
        val data: Data = Data.Builder().putInt(MAX_ROW, config.maxRow).build()
        val irisConstraint = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<SendDataWorker>()
                .setInputData(data)
                .addTag(WORKER_SEND_DATA)
                .setConstraints(irisConstraint)
                .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<SendDataWorker>(config.intervals, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag(WORKER_SEND_DATA)
                .setConstraints(irisConstraint)
                .build()

        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
        WorkManager.getInstance().enqueueUniquePeriodicWork(WORKER_SEND_DATA, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
    }
}