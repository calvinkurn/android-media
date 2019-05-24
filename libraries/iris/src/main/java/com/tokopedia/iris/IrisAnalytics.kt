package com.tokopedia.iris

import android.content.Context
import android.content.Intent
import android.util.Log
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.model.Configuration
import com.tokopedia.iris.worker.IrisService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

/**
 * @author okasurya on 10/2/18.
 */
class IrisAnalytics(val context: Context) : Iris, CoroutineScope {
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
        val intent = Intent(context, IrisService::class.java)
        intent.putExtra(WORKER_SEND_DATA, config)
        context.startService(intent)
    }
}
