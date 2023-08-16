package com.tokopedia.iris.data.db.mapper

import android.os.Build
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.iris.IrisPerformanceData
import com.tokopedia.iris.data.db.table.PerformanceTracking
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.util.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.lang.Exception

/**
 * Created by meta on 23/11/18.
 */
class TrackingMapper {

    fun transformSingleEvent(
        track: String,
        sessionId: String,
        userId: String,
        deviceId: String,
        cache: Cache
    ): String {

        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        val event = JSONArray()

        event.put(reformatEvent(track, sessionId, cache))

        row.put(DEVICE_ID, deviceId)
        row.put(USER_ID, userId)
        row.put(EVENT_DATA, event)
        row.put(APP_VERSION, "$ANDROID_DASH${GlobalConfig.VERSION_NAME}")

        data.put(row)

        result.put("data", data)
        return result.toString()
    }

    fun transformListEvent(tracking: List<Tracking>): Pair<String, List<Tracking>> {
        val result = JSONObject()
        val data = JSONArray()
        var event = JSONArray()
        val outputTracking = mutableListOf<Tracking>()
        var done = false
        for (i in tracking.indices) {
            val item = tracking[i]
            if (!done && !item.event.isBlank() && (item.event.contains("event"))) {
                val eventObject = JSONObject(item.event)
                event.put(eventObject)
                val nextItem: Tracking? = try {
                    tracking[i + 1]
                } catch (e: IndexOutOfBoundsException) {
                    null
                }
                val nextUserId: String = nextItem?.userId ?: ""
                val nextVersion: String = nextItem?.appVersion ?: ""
                // this is to group iris data based on userId and appVersion
                if (item.userId != nextUserId || item.appVersion != nextVersion || i == tracking.size - 1) {
                    val row = JSONObject()
                    row.put(DEVICE_ID, item.deviceId)
                    row.put(USER_ID, item.userId)
                    if (item.appVersion.isEmpty()) {
                        row.put(
                            APP_VERSION,
                            ANDROID_DASH + GlobalConfig.VERSION_NAME + " " + ANDROID_PREV_VERSION_SUFFIX
                        )
                    } else {
                        row.put(APP_VERSION, ANDROID_DASH + item.appVersion)
                    }
                    if (event.length() > 0) {
                        row.put(EVENT_DATA, event)
                        data.put(row)
                        outputTracking.addAll(tracking.subList(0, i + 1))
                    }
                    event = JSONArray()
                    done = true
                }
            }
        }
        result.put("data", data)
        return (result.toString() to outputTracking)
    }

    @Suppress("SwallowedException")
    fun transformListPerfEvent(tracking: List<PerformanceTracking>): Pair<String, List<PerformanceTracking>> {
        val result = JSONObject()
        val data = JSONArray()
        var event = JSONArray()
        val outputTracking = mutableListOf<PerformanceTracking>()
        var done = false
        for (i in tracking.indices) {
            val item = tracking[i]
            if (!done && !item.event.isBlank() && (item.event.contains("event"))) {
                val eventObject = JSONObject(item.event)
                event.put(eventObject)
                val nextItem: PerformanceTracking? = try {
                    tracking[i + 1]
                } catch (e: IndexOutOfBoundsException) {
                    null
                }
                val nextUserId: String = nextItem?.userId ?: ""
                val nextVersion: String = nextItem?.appVersion ?: ""
                // this is to group iris data based on userId and appVersion
                if (item.userId != nextUserId || item.appVersion != nextVersion || i == tracking.size - 1) {
                    val row = JSONObject()
                    row.put(DEVICE_ID, item.deviceId)
                    row.put(USER_ID, item.userId)

                    row.put(
                        KEY_APP,
                        JSONObject().apply {
                            put(
                                KEY_VERSION, if (item.appVersion.isEmpty()) {
                                    ANDROID_DASH + GlobalConfig.VERSION_NAME + " " + ANDROID_PREV_VERSION_SUFFIX
                                } else {
                                    ANDROID_DASH + item.appVersion
                                }
                            )
                            put(KEY_BUILD_NUMBER, GlobalConfig.VERSION_CODE.toString())
                        }
                    )
                    row.put(
                        KEY_DEVICE,
                        JSONObject().apply {
                            put(KEY_CARRIER, item.carrier)
                            put(KEY_BRAND, DeviceInfo.getManufacturerName())
                            put(KEY_MODEL, DeviceInfo.getModelName())
                            put(KEY_OS_VERSION, Build.VERSION.SDK_INT.toString())
                            put(KEY_LOW_POWER, item.lowPower)
                        }
                    )
                    if (event.length() > 0) {
                        row.put(EVENT_DATA, event)
                        data.put(row)
                        outputTracking.addAll(tracking.subList(0, i + 1))
                    }
                    event = JSONArray()
                    done = true
                }
            }
        }
        result.put("data", data)
        return (result.toString() to outputTracking)
    }

    companion object {

        const val DEVICE_ID = "device_id"
        const val USER_ID = "user_id"
        const val EVENT_DATA = "event_data"
        const val APP_VERSION = "app_version"
        const val KEY_VERSION = "version"
        const val KEY_BUILD_NUMBER = "build_number"
        const val KEY_APP = "app"
        const val KEY_DEVICE = "device"
        const val KEY_CARRIER = "carrier"
        const val KEY_BRAND = "brand"
        const val KEY_MODEL = "model"
        const val KEY_OS_VERSION = "os_version"
        const val KEY_LOW_POWER = "low_power"
        const val ANDROID_DASH = "android-"
        const val ANDROID_PREV_VERSION_SUFFIX = "before"
        const val EVENT_OPEN_SCREEN = "openScreen"
        const val KEY_NEW_VISIT = "newVisit"

        fun reformatEvent(event: String, sessionId: String, cache: Cache): JSONObject {
            return try {
                var valueEvent = VALUE_EVENT_MAINAPP
                if (GlobalConfig.isSellerApp()) {
                    valueEvent = VALUE_EVENT_SELLERAPP
                }
                val item = JSONObject(event)
                if (item.has(KEY_EVENT)) {
                    val eventName = item.get(KEY_EVENT)
                    if (!cache.hasVisit() && eventName == EVENT_OPEN_SCREEN) {
                        item.put(KEY_NEW_VISIT, "1")
                        cache.setVisit()
                    }
                    item.put(KEY_EVENT_GA, eventName)
                    item.remove(KEY_EVENT)
                }
                item.put("iris_session_id", sessionId)
                item.put("container", KEY_CONTAINER)
                item.put(KEY_EVENT, valueEvent)
                val hits_time = Calendar.getInstance().timeInMillis
                item.put("hits_time", hits_time)
                try {
                    hits_time.toString().toLong()
                } catch (e: Exception) {
                    ServerLogger.log(
                        Priority.P1,
                        "IRIS",
                        mapOf(
                            "type" to "hitsTimeInvalid",
                            "value" to hits_time.toString(),
                            "exception" to e.toString()
                        )
                    )
                }
                item
            } catch (e: JSONException) {
                JSONObject()
            }
        }

        @Suppress("SwallowedException")
        fun reformatPerformanceEvent(
            irisPerformanceData: IrisPerformanceData,
            sessionId: String
        ): JSONObject {
            return try {
                if (irisPerformanceData.isDataInvalid()) {
                    return JSONObject()
                }
                return JSONObject().apply {
                    put(KEY_SCREEN, irisPerformanceData.screenName)
                    put(KEY_EVENT, VALUE_EVENT_PERFORMANCE)
                    put(KEY_EVENT_GA, VALUE_EVENT_PERFORMANCE)
                    put(KEY_METRICS, JSONArray().apply {
                        put(JSONObject().apply {
                            put(KEY, "ttfl")
                            put(VALUE, irisPerformanceData.ttflInMs)
                        })
                        put(JSONObject().apply {
                            put(KEY, "ttil")
                            put(VALUE, irisPerformanceData.ttilInMs)
                        })
                    })
                    put("iris_session_id", sessionId)
                    put("container", KEY_CONTAINER)
                    val hits_time = Calendar.getInstance().timeInMillis
                    put("hits_time", hits_time)
                }
            } catch (e: JSONException) {
                JSONObject()
            }
        }
    }
}