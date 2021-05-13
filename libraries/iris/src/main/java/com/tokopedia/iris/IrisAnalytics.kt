package com.tokopedia.iris

import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.ConfigurationMapper
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.util.*
import com.tokopedia.iris.worker.IrisWorker
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
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
                ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "CoroutineExceptionIrisAnalytics", "err" to ex.toString()))
            }

    private var remoteConfigListener: RemoteConfig.Listener = object : RemoteConfig.Listener {

        override fun onError(e: java.lang.Exception?) {
            val configuration = Configuration()
            setService(configuration)
        }

        override fun onComplete(remoteConfig: RemoteConfig?) {
            setConfiguration(remoteConfig)
        }
    }

    fun setConfiguration(remoteConfig: RemoteConfig?) {
        val irisEnable = remoteConfig?.getBoolean(RemoteConfigKey.IRIS_GTM_ENABLED_TOGGLE, true)
                ?: true
        val irisConfig = remoteConfig?.getString(RemoteConfigKey.IRIS_GTM_CONFIG_TOGGLE, DEFAULT_CONFIG)
                ?: ""

        setService(irisConfig, irisEnable)
    }

    override fun initialize() {
        remoteConfig = FirebaseRemoteConfigImpl(context)
        remoteConfig.fetch(remoteConfigListener)

        isAlarmOn = false
    }

    override fun setService(config: String, isEnabled: Boolean) {
        try {
            cache.setEnabled(isEnabled)
            val confParse = ConfigurationMapper().parse(config)
            if (confParse != null) {
                this.configuration = confParse
            } else {
                this.configuration = Configuration()
            }
            this.configuration?.isEnabled = isEnabled
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
                    ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "saveEvent", "err" to e.toString()))
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
                    ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "saveEvent", "err" to e.toString()))
                }
            }
        }
    }

    suspend fun saveEventSuspend(map: Map<String, Any>) {
        val trackingRepository = TrackingRepository(context)

        val eventName = map["event"] as? String

        // convert map to json then save as string
        val event = gson.toJson(map)
        val resultEvent = TrackingMapper.reformatEvent(event, session.getSessionId())
        if (WhiteList.REALTIME_EVENT_LIST.contains(eventName) && trackingRepository.getRemoteConfig().getBoolean(KEY_REMOTE_CONFIG_SEND_REALTIME, false)) {
            sendEvent(map)
        } else {
            trackingRepository.saveEvent(resultEvent.toString(), session)
            setAlarm(true, force = false)
        }
    }

    @Deprecated(message = "function should not be called directly outside IrisAnalytics", replaceWith = ReplaceWith(expression = "saveEvent(input)"))
    override fun sendEvent(map: Map<String, Any>) {
        if (cache.isEnabled()) {
            launch(coroutineContext) {
                val trackingRepository = TrackingRepository(context)
                trackingRepository.sendSingleEvent(gson.toJson(map), session, map["event"] as? String)
            }
        }
    }

    /**
     * force = true means run the service now and activate the alarm if any
     */
    override fun setAlarm(isTurnOn: Boolean, force: Boolean) {
        if (configuration == null) {
            if(!::remoteConfig.isInitialized) {
                remoteConfig = FirebaseRemoteConfigImpl(context)
            }
            setConfiguration(remoteConfig)
        }
        val conf = configuration ?: return
        launch(coroutineContext) {
            if (isTurnOn) {
                IrisWorker.scheduleWorker(context, conf, force)
            } else {
                IrisWorker.cancel(context)
            }
        }
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
