package com.tokopedia.iris.data

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.tokopedia.analytics.debugger.IrisLogger
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.util.*
import com.tokopedia.iris.worker.IrisService
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository(
    private val context: Context
) {

    private val cache: Cache = Cache(context)
    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()
    private var firebaseRemoteConfig: RemoteConfig? = null

    private val apiService by lazy {
        ApiService(context).makeRetrofitService()
    }

    fun getRemoteConfig(): RemoteConfig {
        if (firebaseRemoteConfig == null) {
            firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        }
        return firebaseRemoteConfig!!
    }

    private fun getLineDBFlush()= getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_DB_FLUSH, 5000)
    private fun getLineDBSend() = getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_DB_SEND, 400)
    private fun getBatchPerPeriod()= getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_BATCH_SEND, 5)

    suspend fun saveEvent(data: String, session: Session,
                          eventName: String?, eventCategory: String?, eventAction: String?) =
        withContext(Dispatchers.IO) {
            try {
                val tracking = Tracking(data, session.getUserId(), session.getDeviceId())
                trackingDao.insert(tracking)
                logIrisStoreAnalytics(eventName, eventCategory, eventAction)
                IrisLogger.getInstance(context).putSaveIrisEvent(tracking.toString())

                val dbCount = trackingDao.getCount()
                if (dbCount >= getLineDBFlush()) {
                    Timber.e("P1#IRIS#dbCountFlush %d lines", dbCount)
                    trackingDao.flush()
                }
                if (dbCount >= getLineDBSend()) {
                    // if the line is big, send it
                    val i = Intent(context, IrisService::class.java)
                    i.putExtra(MAX_ROW, DEFAULT_MAX_ROW)
                    IrisService.enqueueWork(context, i)

                    IrisAnalytics.getInstance(context).setAlarm(true, force = true)
                    Timber.w("P1#IRIS#dbCountSend %d lines", dbCount)
                }
            } catch (e: Throwable) {
                Timber.e("P1#IRIS#saveEvent %s", e.toString())
            }
        }

    private fun logIrisStoreAnalytics(eventName: String?, eventCategory: String?, eventAction: String?) {
        try {
            if ("clickTopNav" == eventName &&
                eventCategory?.startsWith("top nav") == true &&
                "click search box" == eventAction) {
                Timber.w("P1#IRIS_COLLECT#IRISSTORE_CLICKSEARCHBOX")
            } else if ("clickPDP" == eventName && "product detail page" == eventCategory &&
                "click - tambah ke keranjang" == eventAction) {
                Timber.w("P1#IRIS_COLLECT#IRISSTORE_PDP_ATC")
            }
        } catch (e: Exception) {
            Timber.e("P1#IRIS#logIrisAnalyticsStore %s", e.toString())
        }
    }

    private fun getFromOldest(maxRow: Int): List<Tracking> {
        return try {
            trackingDao.getFromOldest(maxRow)
        } catch (e: Throwable) {
            Timber.e("P1#IRIS#getFromOldest %s", e.toString())
            ArrayList()
        }
    }

    fun delete(data: List<Tracking>) {
        try {
            trackingDao.delete(data)
        } catch (ignored: Throwable) {
            Timber.e("P1#IRIS#deletingData %s", ignored.toString())
        }
    }

    suspend fun sendSingleEvent(data: String, session: Session): Boolean {
        val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(), session.getUserId(), session.getDeviceId())
        val requestBody = ApiService.parse(dataRequest)
        val response = apiService.sendSingleEventAsync(requestBody)
        val isSuccessFul = response.isSuccessful
        if (!isSuccessFul) {
            Timber.e("P1#IRIS#sendSingleEventNotSuccess %s", data)
        }
        return isSuccessFul
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
        val maxLoop = getBatchPerPeriod()
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
            val response = apiService.sendMultiEventAsync(requestBody)
            if (response.isSuccessful && response.code() == 200) {
                IrisLogger.getInstance(context).putSendIrisEvent(data.size.toString() +
                    " - " +
                    request)
                delete(data)
                totalSentData += data.size

                // no need to loop, because it is already less than max row
                if (data.size < maxRow) {
                    break
                }
            } else {
                lastSuccessSent = false
                Timber.e("P1#IRIS#failedSendData %s", requestBody.toString())
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
