package com.tokopedia.iris

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.IrisBroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext


/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(val context: Context) : Iris, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
    private val session: Session = IrisSession(context)
    private var cache: Cache = Cache(context)

    override fun setService(config: String, isEnabled: Boolean) {
        try {
            cache.setEnabled(isEnabled)
            if (cache.isEnabled()) {
                val configuration = ConfigurationMapper().parse(config)
                if (configuration != null) {
                    setWorkManager(configuration)
                }
            }
        } catch(ignored: Exception) { }
    }

    override fun setService(config: Configuration) {
        try {
            cache.setEnabled(config.isEnabled)
            if (cache.isEnabled()) {
                setWorkManager(config)
            }
        } catch(ignored: Exception) { }
    }

    override fun resetService(config: Configuration) {
        try {
            if (cache.isEnabled()) {
                setWorkManager(config)
            }
        } catch (ignored: Exception) {

        }
    }

    override fun saveEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launchCatchError {
                val trackingRepository = TrackingRepository(context)
                // convert map to json then save as string
                val event = JSONObject(map).toString()
                val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
                trackingRepository.saveEvent(resultEvent.toString(), session)
            }
        }
    }

    override fun sendEvent(map: Map<String, Any>) {
         if (cache.isEnabled()) {
            launchCatchError {
                val trackingRepository = TrackingRepository(context)
                val isSuccess = trackingRepository.sendSingleEvent(JSONObject(map).toString(), session)
                if (isSuccess && BuildConfig.DEBUG) {
                     Log.e("Iris", "Success Send Single Event")
                }
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
        val intent = Intent(context, IrisBroadcastReceiver::class.java)
        intent.putExtra(MAX_ROW, config.maxRow)
        val pintent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), TimeUnit.MINUTES.toMillis(config.intervals), pintent)
    }

    companion object {

        private val lock = Any()

        @Volatile private var iris: Iris? = null

        @JvmStatic
        fun getInstance(context: Context) : Iris {
            return iris?: synchronized(lock) {
                IrisAnalytics(context).also {
                    iris = it
                }
            }
        }

        fun deleteInstance() {
            synchronized(lock) {
                iris = null
            }
        }
    }
}
