package com.tokopedia.iris.worker

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.support.v4.app.JobIntentService
import android.util.Log
import android.widget.Toast
import com.tokopedia.iris.WORKER_SEND_DATA
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.model.Configuration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by meta on 24/05/19.
 */
class IrisService : JobIntentService() {

    private val mTimer = Timer()
    private lateinit var mContext: Context

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, IrisService::class.java, 1500, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        toast("Start work")
        try {
            Log.d("Iris", "onHandleWork")
            val configuration = intent.getParcelableExtra<Configuration>(WORKER_SEND_DATA)
            if (configuration != null) {
                startService(configuration)
            }
        } catch (e: java.lang.Exception) {}
    }

    private fun startService(configuration: Configuration) {
        Log.d("Iris", "startService TimerTask")
        mTimer.scheduleAtFixedRate(IrisTask(configuration), 0, TimeUnit.MINUTES.toMillis(1))
    }

    private inner class IrisTask(val configuration: Configuration) : TimerTask() {
        override fun run() {
            send(configuration.maxRow)
            Log.d("Iris", "configuration.maxRow" + configuration.maxRow.toString())
        }
    }

    private fun send(maxRow: Int) {
        val trackingRepository = TrackingRepository(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

        Log.d("Iris", "Send Tracking")
        if (trackings.isNotEmpty()) {

            Log.d("Iris", "Send Tracking isNotEmpty")
            try {
                val request: String = TrackingMapper().transformListEvent(trackings)

                val service = ApiService(mContext).makeRetrofitService()
                val requestBody = ApiService.parse(request)
                service.sendMultiEvent(requestBody).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("Iris", "Send Tracking onFailure")
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful && response.code() == 200) {
                            trackingRepository.delete(trackings)
                            Log.d("Iris", "Send Tracking isSuccessful")
                        }
                    }

                })
            } catch (e: Exception) {
                // no op
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.cancel()
    }

    private val mHandler = Handler()

    // Helper for showing tests
    private fun toast(text: CharSequence) {
        mHandler.post { Toast.makeText(this@IrisService, text, Toast.LENGTH_SHORT).show() }
    }
}
