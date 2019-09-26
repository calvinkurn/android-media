package com.tokopedia.iris.data

import android.content.Context
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.util.Cache
import com.tokopedia.iris.util.DATABASE_NAME
import com.tokopedia.iris.util.Session
import com.tokopedia.iris.util.logIris
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import android.net.ConnectivityManager
import com.tokopedia.iris.data.network.ApiInterface


/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository(
    private val context: Context
) {

    private val cache: Cache = Cache(context)
    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()

    suspend fun saveEvent(data: String, session: Session) = withContext(Dispatchers.IO) {
        try {
            if (isSizeOver()) { // check if db over 2 MB
                trackingDao.flush()
                logIris(cache, "Database Local Over 2mb")
            }
            trackingDao.insert(Tracking(data, session.getUserId(),
                session.getDeviceId() ?: ""))
        } catch (e: Throwable) {
        }
    }

    private fun getFromOldest(maxRow: Int): List<Tracking> {
        return try {
            trackingDao.getFromOldest(maxRow)
        } catch (e: Throwable) {
            ArrayList()
        }
    }

    fun delete(data: List<Tracking>) {
        try {
            trackingDao.delete(data)
            logIris(cache, "Discard: $data")
        } catch (e: Throwable) {
        }
    }

    suspend fun sendSingleEvent(data: String, session: Session): Boolean {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(), session.getUserId(), session.getDeviceId())
        val service = ApiService(context).makeRetrofitService()
        val requestBody = ApiService.parse(dataRequest)
        val request = service.sendSingleEventAsync(requestBody)
        val response = request.await()
        return response.isSuccessful
    }

    private fun isSizeOver(): Boolean {
        val f: File? = context.getDatabasePath(DATABASE_NAME)
        if (f != null) {
            val lengthDb = f.length()
            logIris(cache, "Length Database: $lengthDb")
            val sizeDbInMb = (lengthDb / 1024) / 1024
            return sizeDbInMb >= 2
        }
        return false
    }

    /**
     * @return data size that has been successfully send to server
     * -1 if the data is failed to send or cache is disabled
     * 0 if no data send because it is already empty
     */
    suspend fun sendRemainingEvent(maxRow: Int): Int {
        if (!cache.isEnabled())
            return -1

        var counterLoop = 0
        val maxLoop = 5
        var totalSentData = 0

        var service: ApiInterface? = null
        var lastSuccessSent = true

        // we want to send {maxLoop} times, or until the data is empty.
        while (counterLoop < maxLoop) {
            if (!isNetworkAvailable()) {
                lastSuccessSent = false
                break
            }
            // get data from database limit {maxRow}
            val data: List<Tracking> = getFromOldest(maxRow)
            if (data.isEmpty()) {
                break
            }
            // transform and send the data to server
            val request: String = TrackingMapper().transformListEvent(data)
            if (service == null) {
                service = ApiService(context).makeRetrofitService()
            }
            val requestBody = ApiService.parse(request)
            val response = service.sendMultiEventAsync(requestBody).await()
            if (response.isSuccessful && response.code() == 200) {
                delete(data)
                totalSentData += data.size
                // no need to loop, because it is already less than max row
                if (data.size < maxRow) {
                    break
                }
            } else {
                lastSuccessSent = false
                break
            }
            counterLoop++
        }
        if (totalSentData == 0 && !lastSuccessSent) {
            return -1
        }
        return totalSentData
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
