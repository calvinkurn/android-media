package com.tokopedia.iris.data

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.tokopedia.analyticsdebugger.debugger.IrisLogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.device.info.DeviceConnectionInfo.isPowerSaveMode
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.iris.WhiteList.CM_REALTIME_EVENT_LIST
import com.tokopedia.iris.data.db.IrisDb
import com.tokopedia.iris.data.db.dao.TrackingDao
import com.tokopedia.iris.data.db.dao.TrackingPerfDao
import com.tokopedia.iris.data.db.mapper.TrackingMapper
import com.tokopedia.iris.data.db.table.PerformanceTracking
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.data.network.ApiService
import com.tokopedia.iris.util.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


/**
 * @author okasurya on 10/25/18.
 */
class TrackingRepository private constructor(
    private val context: Context
) {

    private val cache: Cache = Cache(context)
    private val userSession: UserSessionInterface = UserSession(context)
    private val trackingDao: TrackingDao = IrisDb.getInstance(context).trackingDao()
    private val trackingPerfDao: TrackingPerfDao = IrisDb.getInstance(context).trackingPerfDao()
    private var firebaseRemoteConfig: RemoteConfig? = null

    private val apiService by lazy {
        ApiService(context).makeRetrofitService()
    }

    fun getRemoteConfig(): RemoteConfig {
        var remoteConfig = firebaseRemoteConfig
        if (remoteConfig == null) {
            remoteConfig = FirebaseRemoteConfigImpl(context)
            firebaseRemoteConfig = remoteConfig
            return remoteConfig
        } else {
            return remoteConfig
        }
    }

    private fun getLineDBFlush() = getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_DB_FLUSH, 5000)
    private fun getLineDBSend() = getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_DB_SEND, 400)
    private fun getBatchPerPeriod() = getRemoteConfig().getLong(REMOTE_CONFIG_IRIS_BATCH_SEND, 5)

    suspend fun saveEvent(data: String) =
        withContext(Dispatchers.IO) {
            try {
                val tracking = Tracking(
                    data, userSession.userId, userSession.deviceId,
                    Calendar.getInstance().timeInMillis, GlobalConfig.VERSION_NAME
                )
                trackingDao.insert(tracking)
                IrisLogger.getInstance(context).putSaveIrisEvent(tracking.toString())
                setRelicLog("getCount", "total count")
                val dbCount = trackingDao.getCount()
                if (dbCount >= getLineDBFlush()) {
                    ServerLogger.log(
                        Priority.P1,
                        "IRIS",
                        mapOf("type" to "dbCountFlush", "no" to dbCount.toString())
                    )
                    trackingDao.flush()
                } else if (dbCount >= getLineDBSend()) {
                    // if the line is big, send it
                    if (dbCount % 5 == 0) {
                        IrisAnalytics.getInstance(context).setAlarm(true, force = true)
                        if (dbCount % 50 == 0) {
                            ServerLogger.log(
                                Priority.P1,
                                "IRIS",
                                mapOf(
                                    "type" to String.format(
                                        Locale.US,
                                        "dbCountSend %d lines",
                                        dbCount
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (e: Throwable) {
                ServerLogger.log(
                    Priority.P1,
                    "IRIS",
                    mapOf("type" to "saveEvent", "err" to e.toString())
                )
            }
        }

    suspend fun savePerformanceEvent(data: String) =
        withContext(Dispatchers.IO) {
            try {
                val tracking = PerformanceTracking(
                    data, userSession.userId, userSession.deviceId,
                    Calendar.getInstance().timeInMillis, GlobalConfig.VERSION_NAME,
                    DeviceConnectionInfo.getCarrierName(context), isPowerSaveMode(context)
                )
                trackingPerfDao.insert(tracking)
                IrisLogger.getInstance(context).putSaveIrisEvent(tracking.toString())
                val dbCount = trackingDao.getCount()
                if (dbCount >= getLineDBFlush()) {
                    ServerLogger.log(
                        Priority.P1,
                        "IRIS",
                        mapOf("type" to "dbCountFlushPerf", "no" to dbCount.toString())
                    )
                    trackingDao.flush()
                } else if (dbCount >= getLineDBSend()) {
                    // if the line is big, send it
                    if (dbCount % 5 == 0) {
                        IrisAnalytics.getInstance(context).setAlarm(true, force = true)
                        if (dbCount % 50 == 0) {
                            ServerLogger.log(
                                Priority.P1,
                                "IRIS",
                                mapOf(
                                    "type" to String.format(
                                        Locale.US,
                                        "dbCountSendPerf %d lines",
                                        dbCount
                                    )
                                )
                            )
                        }
                    }
                }
            } catch (e: Throwable) {
                ServerLogger.log(
                    Priority.P1,
                    "IRIS",
                    mapOf("type" to "saveEventPerf", "err" to e.toString())
                )
            }
        }

    private fun getFromOldest(maxRow: Int): List<Tracking> {
        return try {
            setRelicLog("getFromOldest", maxRow.toString())
            trackingDao.getFromOldest(maxRow)
        } catch (e: Throwable) {
            ServerLogger.log(
                Priority.P1,
                "IRIS",
                mapOf("type" to String.format(Locale.US, "getFromOldest %s", e.toString()))
            )
            ArrayList()
        }
    }

    private fun getFromOldestPerf(maxRow: Int): List<PerformanceTracking> {
        return try {
            trackingPerfDao.getFromOldest(maxRow)
        } catch (e: Throwable) {
            ServerLogger.log(
                Priority.P1,
                "IRIS",
                mapOf("type" to String.format(Locale.US, "getFromOldestPerf %s", e.toString()))
            )
            ArrayList()
        }
    }

    private fun setRelicLog(queryName: String, queryParam: String) {
        if (getRemoteConfig().getBoolean(RemoteConfigKey.ENABLE_CURSOR_EMBRACE_LOGGING) ?: false) {
            val relicMap = mapOf(
                "queryName" to queryName,
                "detail" to queryParam
            )
            ServerLogger.log(Priority.P2, NEW_RELIC_CUSTOMER_TAG, relicMap)
        }
    }

    fun delete(data: List<Tracking>) {
        try {
            trackingDao.delete(data)
        } catch (ignored: Throwable) {
            ServerLogger.log(
                Priority.P1,
                "IRIS",
                mapOf("type" to String.format(Locale.US, "deletingData %s", ignored.toString()))
            )
        }
    }

    fun deletePerf(data: List<PerformanceTracking>) {
        try {
            trackingPerfDao.delete(data)
        } catch (ignored: Throwable) {
            ServerLogger.log(
                Priority.P1,
                "IRIS",
                mapOf("type" to String.format(Locale.US, "deletingDataPerf %s", ignored.toString()))
            )
        }
    }

    suspend fun sendSingleEvent(data: String, session: Session, eventName: String?): Boolean {
        try {
            val dataRequest = TrackingMapper().transformSingleEvent(
                data, session.getSessionId(),
                userSession.userId, userSession.deviceId,
                cache
            )
            val requestBody = ApiService.parse(dataRequest)
            val response = apiService.sendSingleEventAsync(requestBody)
            val isSuccessFul = response.isSuccessful
            if (!isSuccessFul) {
                ServerLogger.log(
                    Priority.P1,
                    "IRIS_REALTIME_ERROR",
                    mapOf("type" to "not_success", "data" to data.take(ERROR_MAX_LENGTH).trim())
                )
                saveRealTimeCmData(eventName, data, session)
            }
            return isSuccessFul
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P1, "IRIS_REALTIME_ERROR", mapOf(
                    "type" to "exception",
                    "data" to data.take(ERROR_MAX_LENGTH).trim(),
                    "err" to Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()
                )
            )
            saveRealTimeCmData(eventName, data, session)
            return false
        }
    }

    private suspend fun saveRealTimeCmData(eventName: String?, data: String, session: Session) {
        try {
            eventName?.let {
                if (CM_REALTIME_EVENT_LIST.contains(it)) {
                    val transformedEvent =
                        TrackingMapper.reformatEvent(data, session.getSessionId(), cache).toString()
                    saveEvent(transformedEvent)
                }
            }
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P1, "IRIS_REALTIME_ERROR", mapOf(
                    "type" to "transform_exception",
                    "data" to data.take(ERROR_MAX_LENGTH).trim(),
                    "err" to Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim()
                )
            )
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
            if (!isNetworkAvailable(context)) {
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
                ServerLogger.log(
                    Priority.P1, "IRIS", mapOf(
                        "type" to "failedSendData",
                        "data" to request.take(ERROR_MAX_LENGTH).trim()
                    )
                )
                break
            }
            counterLoop++
        }
        if (totalSentData == 0 && !lastSuccessSent) {
            return -1
        }
        return totalSentData
    }

    suspend fun sendRemainingPerfEvent(maxRow: Int): Int {
        if (!cache.isPerformanceEnabled())
            return -1

        var counterLoop = 0
        val maxLoop = getBatchPerPeriod()
        var totalSentData = 0

        var lastSuccessSent = true

        // we want to send {maxLoop} times, or until the data is empty.
        while (counterLoop < maxLoop) {
            if (!isNetworkAvailable(context)) {
                lastSuccessSent = false
                break
            }
            // get data from database limit {maxRow}
            val data: List<PerformanceTracking> = getFromOldestPerf(maxRow)
            if (data.isEmpty()) {
                break
            }
            // transform and send the data to server
            val (request, output) = TrackingMapper().transformListPerfEvent(data)
            val requestBody = ApiService.parse(request)
            val response = apiService.sendMultiEventAsync(requestBody)
            if (response.isSuccessful && response.code() == 200) {
                IrisLogger.getInstance(context).putSendIrisEvent(request, output.size)
                deletePerf(output)
                totalSentData += output.size

                // no need to loop, because it is already less than max row
                if (output.size < maxRow) {
                    break
                }
            } else {
                lastSuccessSent = false
                ServerLogger.log(
                    Priority.P1, "IRIS", mapOf(
                        "type" to "failedSendDataPerf",
                        "data" to request.take(ERROR_MAX_LENGTH).trim()
                    )
                )
                break
            }
            counterLoop++
        }
        if (totalSentData == 0 && !lastSuccessSent) {
            return -1
        }
        return totalSentData
    }

    companion object {
        const val ERROR_MAX_LENGTH = 1000
        const val NEW_RELIC_CUSTOMER_TAG = "CURSOR_INDEX_OUTOFBOUND"

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        private val lock = Any()

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var repository: TrackingRepository? = null

        @JvmStatic
        fun getInstance(context: Context): TrackingRepository {
            return repository ?: synchronized(lock) {
                TrackingRepository(context.applicationContext).also {
                    repository = it
                }
            }
        }
    }
}
