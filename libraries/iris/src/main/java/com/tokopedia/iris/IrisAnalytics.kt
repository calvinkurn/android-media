package com.tokopedia.iris

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.google.gson.Gson
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.util.*
import com.tokopedia.iris.worker.IrisBroadcastReceiver
import com.tokopedia.iris.worker.IrisService
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(val context: Context) : Iris, CoroutineScope {

    private val session: Session = IrisSession(context)
    private var cache: Cache = Cache(context)
    private var configuration: Configuration? = null
    private var isAlarmOn: Boolean = false

    private val gson = Gson()
    private lateinit var remoteConfig: RemoteConfig

    override val coroutineContext: CoroutineContext = Dispatchers.IO +
        CoroutineExceptionHandler { _, ex ->
            Timber.e("P1#IRIS#CoroutineExceptionIrisAnalytics %s", ex.toString())
        }

    private var remoteConfigListener: RemoteConfig.Listener = object : RemoteConfig.Listener {

        override fun onError(e: java.lang.Exception?) {
            val configuration = Configuration()
            setService(configuration)
        }

        override fun onComplete(remoteConfig: RemoteConfig?) {
            val irisEnable = remoteConfig?.getBoolean(RemoteConfigKey.IRIS_GTM_ENABLED_TOGGLE, true)
                ?: true
            val irisConfig = remoteConfig?.getString(RemoteConfigKey.IRIS_GTM_CONFIG_TOGGLE, DEFAULT_CONFIG)
                ?: ""

            setService(irisConfig, irisEnable)
        }
    }

    override fun initialize() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.fetch(remoteConfigListener)

        isAlarmOn = false
    }

    override fun setService(config: String, isEnabled: Boolean) {
        try {
            cache.setEnabled(isEnabled)
            if (cache.isEnabled()) {
                val configuration = ConfigurationMapper().parse(config)
                if (configuration != null) {
                    this.configuration = configuration
                }
            }
        } catch (ignored: Exception) {
        }
    }

    override fun setService(config: Configuration) {
        try {
            cache.setEnabled(config.isEnabled)
            if (cache.isEnabled()) {
                this.configuration = config
            }
        } catch (ignored: Exception) {
        }
    }

    override fun saveEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launch(coroutineContext) {
                try {
                    saveEventSuspend(map)
                } catch (e: Exception) {
                    Timber.e("P1#IRIS#saveEvent %s", e.toString())
                }
            }
        }
    }

    override fun saveEvent(bundle: Bundle) {
        if (cache.isEnabled()) {
            launch(coroutineContext) {
                try {
                    saveEventSuspend(Utils.bundleToMap(bundle))
                } catch (e: Exception) {
                    Timber.e("P1#IRIS#saveEvent %s", e.toString())
                }
            }
        }
    }

    suspend fun saveEventSuspend(map: Map<String, Any>){
        val trackingRepository = TrackingRepository(context)

        val eventName = map["event"] as? String
        val eventCategory = map["eventCategory"] as? String
        val eventAction = map["eventAction"] as? String

        // convert map to json then save as string
        val event = gson.toJson(map)
        val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
        if(WhiteList.REALTIME_EVENT_LIST.contains(eventName) && trackingRepository.getRemoteConfig().getBoolean(KEY_REMOTE_CONFIG_SEND_REALTIME, false)){
            sendEvent(map)
        } else {
            trackingRepository.saveEvent(resultEvent.toString(), session, eventName, eventCategory, eventAction)
            setAlarm(true, force = false)
        }
    }

    @Deprecated(message = "function should not be called directly", replaceWith = ReplaceWith(expression = "saveEvent(input)"))
    override fun sendEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launch(coroutineContext) {
                val trackingRepository = TrackingRepository(context)
                trackingRepository.sendSingleEvent(gson.toJson(map), session)
            }
        }
    }

    override fun setAlarm(isTurnOn: Boolean, force: Boolean) {
        if (!force && isTurnOn == isAlarmOn) {
            return
        }
        val pendingIntent: PendingIntent?
        pendingIntent = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val intent = Intent(context, IrisService::class.java)
            intent.putExtra(MAX_ROW, this.configuration?.maxRow ?: DEFAULT_MAX_ROW)
            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            val intent = Intent(context, IrisBroadcastReceiver::class.java)
            intent.putExtra(MAX_ROW, this.configuration?.maxRow ?: DEFAULT_MAX_ROW)
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        pendingIntent?.let {
            val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (isTurnOn) {
                alarm.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                    TimeUnit.MINUTES.toMillis(this.configuration?.intervals
                        ?: DEFAULT_SERVICE_TIME), pendingIntent)
            } else {
                alarm.cancel(pendingIntent)
            }
        }
        isAlarmOn = isTurnOn
    }

    override fun getSessionId(): String {
        return session.getSessionId()
    }

    companion object {

        const val KEY_REMOTE_CONFIG_SEND_REALTIME = "android_customerapp_iris_realtime"

        private val lock = Any()

        @Volatile
        private var iris: Iris? = null

        @JvmStatic
        fun getInstance(context: Context): Iris {
            val applicationContext = context.applicationContext
            return iris ?: synchronized(lock) {
                IrisAnalytics(applicationContext).also {
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
