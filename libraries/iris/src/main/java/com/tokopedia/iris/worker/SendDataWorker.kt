package com.tokopedia.iris.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.iris.MAX_ROW
import com.tokopedia.iris.data.TrackingRepository
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import rx.schedulers.Schedulers

/**
 * @author okasurya on 10/18/18.
 */
class SendDataWorker(val context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("Oka Worker", "doWork()")
        val maxRow = inputData.getInt(MAX_ROW, 0)

        val trackingRepository: TrackingRepository = TrackingRepository.getInstance(applicationContext)

        val trackings: List<Tracking> = trackingRepository.getFromOldest(maxRow)

        var listener = Result.FAILURE
        if (trackings.isNotEmpty()) {

            val request: String = TrackingMapper(context).transform(trackings)

            Log.d("Oka Worker", "doWork() Hit Server $request")

            trackingRepository.apiService.apiInterface.sendMultiEvent(request)
                    .subscribeOn(Schedulers.io())
                    .subscribe {
                        if (it.isSuccessful) {
                            trackingRepository.delete(trackings)
                            listener = Result.SUCCESS
                        }
                    }
        }
        return listener
    }
}