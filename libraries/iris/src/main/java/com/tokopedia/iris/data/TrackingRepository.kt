package com.tokopedia.iris.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.tokopedia.iris.IrisSession
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import retrofit2.Response
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository private constructor(
       val context: Context
) {

    val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    val apiService: ApiService = ApiService(context)

    fun saveEvent(data: String, sessionId: String, userId: String) = trackingDao.insert(Tracking(data, sessionId, userId))

    fun getFromOldest(maxRow: Int) = trackingDao.getFromOldest(maxRow)

    fun delete(data: List<Tracking>) = trackingDao.delete(data)

    fun sendSingleEvent(data: String, sessionId: String, userId: String) {
        val request = TrackingMapper(context).transform(data, sessionId, userId)
        apiService.apiInterface.sendSingleEvent(request)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    if (it.isSuccessful) {
                        Log.d("Iris", it.message())
                    }
                }
    }

    companion object {
        // For Singleton instantiation
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: TrackingRepository? = null

        fun getInstance(context: Context) =
                instance ?: synchronized(this) {
                    instance ?: TrackingRepository(context).also { instance = it }
                }
    }
}
