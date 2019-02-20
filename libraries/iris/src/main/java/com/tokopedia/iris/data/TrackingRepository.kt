package com.tokopedia.iris.data

import android.content.Context
import android.util.Log
import com.tokopedia.iris.*
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiInterface
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import java.io.File

/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository (
        private val context: Context
) {

    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    fun saveEvent(data: String, session: Session) {

        if (isSizeOver()) { // check if db over 2 MB
            trackingDao.flush()
        }

        trackingDao.insert(Tracking(data, session.getUserId(), session.getDeviceId()))
    }

    fun getFromOldest(maxRow: Int) = trackingDao.getFromOldest(maxRow)

    fun delete(data: List<Tracking>) = trackingDao.delete(data)

    fun sendSingleEvent(data: String, session: Session) {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(), session.getUserId(), session.getDeviceId())
        val service = ApiService(context).makeRetrofitService()
        val apiService = service.create(ApiInterface::class.java)
        GlobalScope.launch {
            val requestBody = ApiService.parse(dataRequest)
            val request = apiService.sendSingleEvent(
                    HEADER_JSON,
                    HEADER_ANDROID,
                    session.getUserId(),
                    requestBody)
            val response = request.await()
            if (response.isSuccessful) {
                Log.d("Iris Service Single", "${response.code()}: response.body().toString()")
            }
        }
    }

    private fun isSizeOver() : Boolean {
        val f: File? = context.getDatabasePath(DATABASE_NAME)
        if (f != null) {
            Log.d("Iris", "Length DB: ${f.length()}")
            val sizeDbInMb = (f.length() / 1024) / 1024
            return sizeDbInMb >= 2
        }
        return false
    }
}
