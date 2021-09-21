package com.tokopedia.iris.data.db.mapper

import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.util.KEY_CONTAINER
import com.tokopedia.iris.util.KEY_EVENT
import com.tokopedia.iris.util.KEY_EVENT_SELLERAPP
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.lang.Exception

/**
 * Created by meta on 23/11/18.
 */
class TrackingMapper {

    fun transformSingleEvent(track: String, sessionId: String, userId: String, deviceId: String): String {

        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        val event = JSONArray()

        event.put(reformatEvent(track, sessionId))

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
                        row.put(APP_VERSION, ANDROID_DASH + GlobalConfig.VERSION_NAME + " " + ANDROID_PREV_VERSION_SUFFIX)
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

    companion object {

        const val DEVICE_ID = "device_id"
        const val USER_ID = "user_id"
        const val EVENT_DATA = "event_data"
        const val APP_VERSION = "app_version"
        const val ANDROID_DASH = "android-"
        const val ANDROID_PREV_VERSION_SUFFIX = "before"

        fun reformatEvent(event: String, sessionId: String): JSONObject {
            return try {
                var keyEvent = KEY_EVENT
                if (GlobalConfig.isSellerApp()) {
                    keyEvent = KEY_EVENT_SELLERAPP
                }
                val item = JSONObject(event)
                if (item.has("event") && item.get("event") != null) {
                    item.put("event_ga", item.get("event"))
                    item.remove("event")
                }
                item.put("iris_session_id", sessionId)
                item.put("container", KEY_CONTAINER)
                item.put("event", keyEvent)
                val hits_time = Calendar.getInstance().timeInMillis
                item.put("hits_time",hits_time)
                try{
                    val _ignore = hits_time.toString().toLong()
                }catch (e: Exception){
                    ServerLogger.log(Priority.P1, "IRIS", mapOf("type" to "hitsTimeInvalid", "value" to hits_time.toString(), "exception" to e.toString()))
                }
                item
            } catch (e: JSONException) {
                JSONObject()
            }
        }
    }
}