package com.tokopedia.iris.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response

/**
 * @author okasurya on 10/18/18.
 */
class SendDataWorker(private val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("Oka Worker", "doWork()")
        val maxRow = inputData.getInt(MAX_ROW, 0)

        val trackingRepository = TrackingRepository(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)


        if (trackings.isNotEmpty()) {

            val request: String = TrackingMapper(context).transform(trackings)

            Log.d("Oka Worker", "doWork() Hit Server $request")

            val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), request)
            val response: Response<String> = trackingRepository.apiService.apiInterface.sendMultiEvent(body)
                    .toBlocking()
                    .last()
            if (response.isSuccessful && response.code() == 200) {
//                trackingRepository.delete(trackings)
                return Result.SUCCESS
            }
        }
        return Result.FAILURE
    }
}