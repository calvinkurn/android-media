package com.tokopedia.iris

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created by meta on 23/01/19.
 */
class TestingService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val maxRow = DEFAULT_MAX_ROW

        if (context != null) {

            val trackingRepository = TrackingRepository(context)

            val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

            if (trackings.isNotEmpty()) {

                val request: String = TrackingMapper().transformListEvent(trackings)

                val service = ApiService(context).makeRetrofitService()

                val response = runBlocking {
                    val requestBody = ApiService.parse(request)
                    val response = service.sendMultiEvent(requestBody)
                    response.await()
                }
                if (response.isSuccessful && response.code() == 200) {
                    trackingRepository.delete(trackings)
                }
            }
        }
    }
}