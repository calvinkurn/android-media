package com.tokopedia.iris.data

import android.content.Context
import android.util.Log
import com.tokopedia.iris.Session
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

    fun saveEvent(data: String, session: Session) = trackingDao.insert(Tracking(data, session.getUserId(), session.getDeviceId()))

    fun getFromOldest(maxRow: Int) = trackingDao.getFromOldest(maxRow)

    fun delete(data: List<Tracking>) = trackingDao.delete(data)

    fun sendSingleEvent(data: String, session: Session) {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(), session.getUserId(), session.getDeviceId())
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
