package com.tokopedia.iris.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import retrofit2.Response
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.runBlocking

/**
 * @author okasurya on 10/18/18.
 */
class SendDataWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val maxRow = inputData.getInt(MAX_ROW, 0)

        val trackingRepository = TrackingRepository(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

        if (trackings.isNotEmpty()) {
            try {
                val request: String = TrackingMapper().transformListEvent(trackings)

                val service = ApiService(context).makeRetrofitService()

                var response: Response<String>? = runBlocking {
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
        return Result.SUCCESS
    }
}