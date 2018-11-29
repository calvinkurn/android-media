package com.tokopedia.iris.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import retrofit2.Response
import rx.schedulers.Schedulers

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

            val response: Response<String> = trackingRepository.apiService.apiInterface.sendMultiEvent(request)
                    .subscribeOn(Schedulers.io())
                    .toBlocking()
                    .last()

            if (response.isSuccessful) {
                trackingRepository.delete(trackings)
                return Result.SUCCESS
            }
        }
        return Result.FAILURE
    }
}