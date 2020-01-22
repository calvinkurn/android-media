package com.tokopedia.iris.data

import android.content.Context
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import android.net.ConnectivityManager
import com.tokopedia.iris.util.*
import timber.log.Timber


/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository(
    private val context: Context
) {

    private val cache: Cache = Cache(context)
    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()
    private val apiService by lazy {
        ApiService(context).makeRetrofitService()
    }

    suspend fun saveEvent(data: String, session: Session) = withContext(Dispatchers.IO) {
        try {
            val dBSize = getSizeDBInKB()
            // if size is over 2MB, flush it
            if (dBSize >= 2000F) {
                trackingDao.flush()
            }
            trackingDao.insert(Tracking(data, session.getUserId(),
                session.getDeviceId() ?: ""))

            if (dBSize >= 500F) {
                // if size already 500KB or more, send it
                sendRemainingEvent(DEFAULT_MAX_ROW)
            }
        } catch (e: Throwable) {
            Timber.e("P2#IRIS#saveEvent %s", e.toString())
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
        } catch (ignored: Throwable) {
        }
    }

    suspend fun sendSingleEvent(data: String, session: Session): Boolean {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(), session.getUserId(), session.getDeviceId())
        val requestBody = ApiService.parse(dataRequest)
        val request = apiService.sendSingleEventAsync(requestBody)
        val response = request.await()
        return response.isSuccessful
    }

    private fun getSizeDBInKB(): Float {
        val f: File? = context.getDatabasePath(DATABASE_NAME)
        return if (f != null) {
            (f.length() / 1_000F)
        } else {
            0F
        }
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
        val maxLoop = 10
        var totalSentData = 0

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
            val requestBody = ApiService.parse(request)
            val response = apiService.sendMultiEventAsync(requestBody).await()
            if (response.isSuccessful && response.code() == 200) {
                delete(data)
                totalSentData += data.size
                // no need to loop, because it is already less than max row
                if (data.size < maxRow) {
                    break
                }
            } else {
                lastSuccessSent = false
                Timber.e("P2#IRIS#failedSendData %s", requestBody.toString())
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
