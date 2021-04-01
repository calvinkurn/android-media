package com.tokopedia.iris.data

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.tokopedia.analyticsdebugger.debugger.IrisLogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.WhiteList.CM_REALTIME_EVENT_LIST
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.util.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
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

    suspend fun saveEvent(data: String, session: Session) =
        withContext(Dispatchers.IO) {
            try {
                val tracking = Tracking(data, userSession.userId, userSession.deviceId,
                    Calendar.getInstance().timeInMillis, GlobalConfig.VERSION_NAME)
                trackingDao.insert(tracking)
                IrisLogger.getInstance(context).putSaveIrisEvent(tracking.toString())

                val dbCount = trackingDao.getCount()
                if (dbCount >= getLineDBFlush()) {
                    ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to String.format("dbCountFlush %d lines", dbCount)))
                    trackingDao.flush()
                } else if (dbCount >= getLineDBSend()) {
                    // if the line is big, send it
                    if (dbCount % 5 == 0) {
                        IrisAnalytics.getInstance(context).setAlarm(true, force = true)
                        if (dbCount % 50 == 0) {
                            ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to String.format("dbCountSend %d lines", dbCount)))
                        }
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
            ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to String.format("getFromOldest %s", e.toString())))
            ArrayList()
        }
    }

    fun delete(data: List<Tracking>) {
        try {
            trackingDao.delete(data)
        } catch (ignored: Throwable) {
            ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to String.format("deletingData %s", ignored.toString())))
        }
    }

    suspend fun sendSingleEvent(data: String, session: Session, eventName: String?): Boolean {
        try {
            val dataRequest = TrackingMapper().transformSingleEvent(data, session.getSessionId(),
                    userSession.userId, userSession.deviceId)
            val requestBody = ApiService.parse(dataRequest)
            val response = apiService.sendSingleEventAsync(requestBody)
            val isSuccessFul = response.isSuccessful
            if (!isSuccessFul) {
                ServerLogger.log(Priority.P1, "IRIS_REALTIME_ERROR", mapOf("type" to "not_success", "data" to data.take(ERROR_MAX_LENGTH).trim()))
                saveRealTimeCmData(eventName, data, session)
            }
            return isSuccessFul
        } catch (e: Exception) {
            ServerLogger.log(Priority.P1, "IRIS_REALTIME_ERROR", mapOf("type" to "exception",
                    "data" to data.take(ERROR_MAX_LENGTH).trim(),
                    "err" to Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()))
            saveRealTimeCmData(eventName, data, session)
            return false
        }
    }

    private suspend fun saveRealTimeCmData(eventName: String?, data: String, session: Session){
        try {
            eventName?.let {
                if (CM_REALTIME_EVENT_LIST.contains(it)) {
                    val transformedEvent = TrackingMapper.reformatEvent(data, session.getSessionId()).toString()
                    saveEvent(transformedEvent, session)
                }
            }
        }catch (e:Exception){
            ServerLogger.log(Priority.P1, "IRIS_REALTIME_ERROR", mapOf("type" to "transform_exception",
                    "data" to data.take(ERROR_MAX_LENGTH).trim(),
                    "err" to Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()))
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
                Timber.e("P1#IRIS#failedSendData;data='${request.take(ERROR_MAX_LENGTH).trim()}'")
                ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "failedSendData",
                        "data" to request.take(ERROR_MAX_LENGTH).trim()))
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
