package com.tokopedia.iris.data.db.mapper

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.tokopedia.iris.data.db.table.Tracking
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by meta on 23/11/18.
 */
class TrackingMapper(context: Context) {

    @SuppressLint("HardwareIds")
    var uniqueDeviceId: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun transform(track: String, sessionId: String, userId: String) : String {

        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        val event = JSONArray()

        val item = JSONObject(track)
        item.put("session_id", sessionId)
        item.put("container", "gtm")
        item.put("event", "default_app")
        event.put(item)

        row.put("device_id", uniqueDeviceId)
        row.put("user_id", userId)
        row.put("event_ga", event)

        data.put(row)

        result.put("data", data)
        return result.toString()
    }

    fun transform(tracking: List<Tracking>) : String {
        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        var event = JSONArray()
        for (i in tracking.indices) {
            val item = tracking[i]
            event.put(transform(item))
            val nextItem: Tracking? = try {
                tracking[i+1]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
            val userId: String = nextItem?.userId ?: ""
            if (userId != item.userId) {
                if (event.length() > 0) {
                    row.put("event_ga", event)
                    data.put(row)
                }
                row.put("device_id", uniqueDeviceId)
                row.put("user_id", item.userId)
                event = JSONArray()
            }
        }
        result.put("data", data)
        return result.toString()
    }

    fun transform(tracking: Tracking) : JSONObject {
        val item = JSONObject(tracking.event)
        item.put("session_id", tracking.sessionId)
        item.put("container", "gtm")
        item.put("event", "default_app")
        return item
    }
}