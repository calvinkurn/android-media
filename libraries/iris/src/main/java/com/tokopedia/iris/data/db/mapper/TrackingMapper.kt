package com.tokopedia.iris.data.db.mapper

import com.tokopedia.iris.KEY_CONTAINER
import com.tokopedia.iris.KEY_EVENT_GA
import com.tokopedia.iris.data.db.table.Tracking
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by meta on 23/11/18.
 */
class TrackingMapper {

    fun transformSingleEvent(track: String, sessionId: String, userId: String, deviceId: String) : String {

        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        val event = JSONArray()

        event.put(addSessionToEvent(track, sessionId))

        row.put("device_id", deviceId)
        row.put("user_id", userId)
        row.put("event_data", event)
        row.put("container", KEY_CONTAINER)
        row.put("event_ga", KEY_EVENT_GA)

        data.put(row)

        result.put("data", data)
        return result.toString()
    }

    fun transformListEvent(tracking: List<Tracking>) : String {
        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        var event = JSONArray()
        for (i in tracking.indices) {
            val item = tracking[i]
            event.put(addSessionToEvent(item.event, item.sessionId))
            val nextItem: Tracking? = try {
                tracking[i+1]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
            val userId: String = nextItem?.userId ?: ""
            if (userId != item.userId) {
                if (event.length() > 0) {
                    row.put("event_data", event)
                    data.put(row)
                }
                row.put("device_id", item.deviceId)
                row.put("user_id", item.userId)
                row.put("container", KEY_CONTAINER)
                row.put("event_ga", KEY_EVENT_GA)
                event = JSONArray()
            }
        }
        result.put("data", data)
        return result.toString()
    }

    fun addSessionToEvent(event: String, sessionId: String) : JSONObject {
        return try {
            var item = JSONObject(event)
            item.put("iris_session_id", sessionId)
            item
        } catch (e: JSONException) {
            JSONObject()
        }
    }
}