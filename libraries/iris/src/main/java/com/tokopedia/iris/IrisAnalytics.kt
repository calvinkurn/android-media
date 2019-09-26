package com.tokopedia.iris

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.util.*
import com.tokopedia.iris.worker.IrisBroadcastReceiver
import com.tokopedia.iris.worker.IrisExecutor
import com.tokopedia.iris.worker.IrisExecutor.handler
import com.tokopedia.iris.worker.IrisService
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    private lateinit var remoteConfig: RemoteConfig

    override val coroutineContext: CoroutineContext by lazy {
        IrisExecutor.executor + handler
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

            val irisLogEnable = remoteConfig?.getBoolean(RemoteConfigKey.IRIS_LOG_ENABLED_TOGGLE, false)
                ?: false
            cache.setEnableLogEntries(irisLogEnable)
        }
    }

    override fun initialize() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.fetch(remoteConfigListener)

        isAlarmOn = cache.isAlarmOn()
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
            launch(coroutineContext + Dispatchers.IO) {
                val trackingRepository = TrackingRepository(context)
                // convert map to json then save as string
                val event = JSONObject(map).toString()
                val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
                trackingRepository.saveEvent(resultEvent.toString(), session)
                setAlarm(true)
            }
        }
    }

    override fun sendEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launch(coroutineContext + Dispatchers.IO) {
                val trackingRepository = TrackingRepository(context)
                val isSuccess = trackingRepository.sendSingleEvent(JSONObject(map).toString(), session)
                if (isSuccess && BuildConfig.DEBUG) {
                    logIris(cache, "Success Send Single Event")
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

    override fun setAlarm(isTurnOn: Boolean) {
        if (isTurnOn == isAlarmOn) {
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
        cache.setEnableAlarm(isAlarmOn)
    }

    companion object {

        private val lock = Any()

        @Volatile
        private var iris: Iris? = null

        @JvmStatic
        fun getInstance(context: Context): Iris {
            return iris ?: synchronized(lock) {
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
