package com.tokopedia.iris.data

import android.content.Context
import android.util.Log
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository (
        private val context: Context
) {

    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    fun saveEvent(data: String, sessionId: String, userId: String) = trackingDao.insert(Tracking(data, sessionId, userId))

    fun getFromOldest(maxRow: Int) = trackingDao.getFromOldest(maxRow)

    fun delete(data: List<Tracking>) = trackingDao.delete(data)

    fun sendSingleEvent(data: String, sessionId: String, userId: String) {
        val dataRequest = TrackingMapper(context).transform(data, sessionId, userId)
        val service = ApiService(context).makeRetrofitService()
        GlobalScope.launch {
            val requestBody = ApiService.parse(dataRequest)
            val request = service.sendSingleEvent(requestBody)
            val response = request.await()
            if (response.isSuccessful) {
                Log.d("Iris", response.body().toString())
            }
        }
    }
}
