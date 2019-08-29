package com.tokopedia.iris.data

import android.content.Context
import com.tokopedia.iris.DATABASE_NAME
import com.tokopedia.iris.Session
import com.tokopedia.iris.TAG
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository (
        private val context: Context
) {

    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    suspend fun saveEvent(data: String, session: Session) = withContext(Dispatchers.IO) {
        try {
            if (isSizeOver()) { // check if db over 2 MB
                trackingDao.flush()
                Timber.d("$TAG Database Local Over 2mb")
            }
            trackingDao.insert(Tracking(data, session.getUserId(), session.getDeviceId()))
        } catch (e: Throwable) {}
    }

    private fun getFromOldest(maxRow: Int) : List<Tracking> {
        return try {
            trackingDao.getFromOldest(maxRow)
        } catch (e: Throwable) {
            ArrayList()
        }
    }

    fun delete(data: List<Tracking>) {
        try {
            trackingDao.delete(data)
            Timber.d("$TAG Discard: $data")
        } catch (e: Throwable) {}
    }

    suspend fun sendSingleEvent(data: String, session: Session) : Boolean {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(),
                session.getUserId(), session.getDeviceId())
        val service = ApiService(context).makeRetrofitService()
        val requestBody = ApiService.parse(dataRequest)
        val request = service.sendSingleEvent(requestBody)
        val response = request.await()
        return response.isSuccessful
    }

    private fun isSizeOver() : Boolean {
        val f: File? = context.getDatabasePath(DATABASE_NAME)
        if (f != null) {
            val lengthDb = f.length()
            Timber.d("$TAG Length Database: $lengthDb")
            val sizeDbInMb = (lengthDb / 1024) / 1024
            return sizeDbInMb >= 2
        }
        return false
    }

    suspend fun sendRemainingEvent(maxRow: Int) {
        val data: List<Tracking> = getFromOldest(maxRow)

        if (data.isNotEmpty()) {
            val request: String = TrackingMapper().transformListEvent(data)

            val service = ApiService(context).makeRetrofitService()
            val requestBody = ApiService.parse(request)
            val response = service.sendMultiEvent(requestBody).await()
            if (response.isSuccessful && response.code() == 200) {
                delete(data)
            }
        }
    }
}
