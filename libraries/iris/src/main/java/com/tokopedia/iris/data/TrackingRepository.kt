package com.tokopedia.iris.data

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.tokopedia.analyticsdebugger.debugger.IrisLogger
import com.tokopedia.config.GlobalConfig
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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository(
    private val context: Context
) {

    private val cache: Cache = Cache(context)
    private val userSession: UserSessionInterface = UserSession(context)
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
                val tracking = Tracking(data, userSession.userId, userSession.deviceId,
                    Calendar.getInstance().timeInMillis, GlobalConfig.VERSION_NAME)
                trackingDao.insert(tracking)
                IrisLogger.getInstance(context).putSaveIrisEvent(tracking.toString())

                val dbCount = trackingDao.getCount()
                if (dbCount >= getLineDBFlush()) {
                    Timber.e("P1#IRIS#dbCountFlush %d lines", dbCount)
                    trackingDao.flush()
                } else if (dbCount >= getLineDBSend()) {
                    // if the line is big, send it
                    if (dbCount % 5 == 0) {
                        val i = Intent(context, IrisService::class.java)
                        i.putExtra(MAX_ROW, DEFAULT_MAX_ROW)
                        IrisService.enqueueWork(context, i)

                        IrisAnalytics.getInstance(context).setAlarm(true, force = true)
                        Timber.w("P1#IRIS#dbCountSend %d lines", dbCount)
                    }
                }
            } catch (e: Throwable) {
                Timber.e("P1#IRIS#saveEvent %s", e.toString())
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
        try {
            val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(),
                    userSession.userId, userSession.deviceId)
            val requestBody = ApiService.parse(dataRequest)
            val response = apiService.sendSingleEventAsync(requestBody)
            val isSuccessFul = response.isSuccessful
            if (!isSuccessFul) {
                Timber.e("P1#IRIS_REALTIME_ERROR#not_success;data='${data.take(ERROR_MAX_LENGTH).trim()}'")
            }
            return isSuccessFul
        } catch (e: Exception) {
            Timber.e("P1#IRIS_REALTIME_ERROR#exception;data='${data.take(ERROR_MAX_LENGTH).trim()}';err='${Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()}'")
            return false
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
            val (request, output) = TrackingMapper().transformListEvent(data)
            val requestBody = ApiService.parse(request)
            val response = apiService.sendMultiEventAsync(requestBody)
            if (response.isSuccessful && response.code() == 200) {
                IrisLogger.getInstance(context).putSendIrisEvent(request, output.size)
                delete(output)
                totalSentData += output.size

                // no need to loop, because it is already less than max row
                if (output.size < maxRow) {
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

    companion object {
        const val ERROR_MAX_LENGTH = 1000
    }
}
