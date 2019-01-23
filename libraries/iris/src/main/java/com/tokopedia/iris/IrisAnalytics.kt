package com.tokopedia.iris

import android.content.Context
import androidx.work.*
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.SendDataWorker
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import android.widget.Toast
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.app.PendingIntent
import android.content.Intent



/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(private val context: Context) : Iris {
    private val trackingRepository: TrackingRepository = TrackingRepository(context)
    private val session: Session = IrisSession(context)

    override fun setService(config: Configuration) {
        GlobalScope.launch {
//            setWorkManager(config)

            val intent = Intent(context, TestingService::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 234, intent, 0)
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000, pendingIntent)
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
        val workRequest = PeriodicWorkRequestBuilder<SendDataWorker>(config.intervals, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag(WORKER_SEND_DATA)
                .setConstraints(irisConstraint)
                .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(WORKER_SEND_DATA, ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }
}