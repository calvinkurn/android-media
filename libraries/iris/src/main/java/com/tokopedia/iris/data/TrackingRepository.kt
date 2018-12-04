package com.tokopedia.iris.data

import android.content.Context
import android.util.Log
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.schedulers.Schedulers

/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository (
        private val context: Context
) {

    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    val apiService: ApiService = ApiService(context)

    fun saveEvent(data: String, sessionId: String, userId: String) = trackingDao.insert(Tracking(data, sessionId, userId))

    fun getFromOldest(maxRow: Int) = trackingDao.getFromOldest(maxRow)

    fun delete(data: List<Tracking>) = trackingDao.delete(data)

    fun sendSingleEvent(data: String, sessionId: String, userId: String) {
        val request = TrackingMapper(context).transform(data, sessionId, userId)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), request)
        apiService.apiInterface.sendSingleEvent(body)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    if (it.isSuccessful) {
                        Log.d("Iris", it.message())
                    }
                }
    }
}
