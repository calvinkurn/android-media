package com.tokopedia.iris.worker

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.tokopedia.iris.WORKER_SEND_DATA
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.model.Configuration
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.util.*

/**
 * Created by meta on 24/05/19.
 */
class IrisService : Service() {

    private val mTimer = Timer()
    private lateinit var mContext: Context

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        try {
            val configuration = intent?.getParcelableExtra<Configuration>(WORKER_SEND_DATA)
            if (configuration != null) {
                startService(configuration)
            }
        } catch (e: java.lang.Exception) {}
        return START_STICKY_COMPATIBILITY
    }

    private fun startService(configuration: Configuration) {
        mTimer.scheduleAtFixedRate(IrisTask(configuration), 0, configuration.intervals)
    }

    private inner class IrisTask(val configuration: Configuration) : TimerTask() {
        override fun run() {
            send(configuration.maxRow)
        }
    }

    private fun send(maxRow: Int) {
        val trackingRepository = TrackingRepository(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

        if (trackings.isNotEmpty()) {
            try {
                val request: String = TrackingMapper().transformListEvent(trackings)

                val service = ApiService(mContext).makeRetrofitService()

                val response: Response<String>? = runBlocking {
                    try {
                        val requestBody = ApiService.parse(request)
                        val response = service.sendMultiEvent(requestBody)
                        response.await()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (response != null
                        && response.isSuccessful
                        && response.code() == 200) {
                    trackingRepository.delete(trackings)
                }
            } catch (e: Exception) {
                // no op
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.cancel()
    }
}
