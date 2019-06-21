package com.tokopedia.iris.worker

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import com.tokopedia.iris.DEFAULT_MAX_ROW
import com.tokopedia.iris.JOB_IRIS_ID
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by meta on 24/05/19.
 */
class IrisService : JobIntentService() {

    private lateinit var mContext: Context

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object {
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, IrisService::class.java, JOB_IRIS_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            val maxRow = intent.getIntExtra(MAX_ROW, DEFAULT_MAX_ROW)
            startService(maxRow)
        } catch (e: java.lang.Exception) {}
    }

    private fun startService(maxRow: Int) {
        val trackingRepository = TrackingRepository(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

        if (trackings.isNotEmpty()) {

            try {
                val request: String = TrackingMapper().transformListEvent(trackings)

                val service = ApiService(mContext).makeRetrofitService()
                val requestBody = ApiService.parse(request)
                service.sendMultiEvent(requestBody).enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) { }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful && response.code() == 200) {
                            Thread {
                                trackingRepository.delete(trackings)
                            }.start()
                        }
                    }

                })
            } catch (e: Exception) {
                // no op
            }
        }
    }
}
