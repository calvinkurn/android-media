package com.tokopedia.iris

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.SendDataWorker
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext


/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(context: Context) : Iris, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private val trackingRepository: TrackingRepository = TrackingRepository(context)
    private val session: Session = IrisSession(context)
    private var cache: Cache = Cache(context)
    
    override fun setService(config: Configuration) {
        try {
            cache.setEnabled(config)
            if (cache.isEnabled()) {
                setWorkManager(config)
            }
        } catch(ignored: Exception) { }
    }

    override fun resetService(config: Configuration) {
        try {
            if (cache.isEnabled()) {
                WorkManager.getInstance().cancelAllWorkByTag(WORKER_SEND_DATA)
                setWorkManager(config)
            }
        } catch (ignored: Exception) {

        }
    }

    override fun saveEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launchCatchError(block = {
                // convert map to json then save as string
                val event = JSONObject(map).toString()
                val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
                trackingRepository.saveEvent(resultEvent.toString(), session)
            }) {
                // no-op
            } 
        }
    }

    override fun sendEvent(map: Map<String, Any>) {
         if (cache.isEnabled()) {
             launchCatchError(block = {
                val isSuccess = trackingRepository.sendSingleEvent(JSONObject(map).toString(),
                        session)
                if (isSuccess && BuildConfig.DEBUG) {
                    Log.e("Iris", "Success Send Single Event")
                }
            }) {
                // no-op
            }
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
