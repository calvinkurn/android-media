package com.tokopedia.iris.data.db.mapper

import com.tokopedia.iris.KEY_CONTAINER
import com.tokopedia.iris.KEY_EVENT
import com.tokopedia.iris.data.db.table.Tracking
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by meta on 23/11/18.
 */
class TrackingMapper {

    fun transformSingleEvent(track: String, sessionId: String, userId: String, deviceId: String) : String {

        val result = JSONObject()
        val data = JSONArray()
        val row = JSONObject()
        val event = JSONArray()

        val timeStamp = Calendar.getInstance().timeInMillis
        event.put(addSessionToEvent(track, sessionId, timeStamp))

        row.put("device_id", deviceId)
        row.put("user_id", userId)
        row.put("event_data", event)

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
            event.put(addSessionToEvent(item.event, item.sessionId, item.timeStamp))
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
                event = JSONArray()
            }
        }
        result.put("data", data)
        return result.toString()
    }

    fun addSessionToEvent(event: String, sessionId: String, timeStamp: Long) : JSONObject {
        return try {
            val item = JSONObject(event)
            if (item.get("event") != null) {
                item.put("event_ga", item.get("event"))
                item.remove("event")
            }
            item.put("iris_session_id", sessionId)
            item.put("container", KEY_CONTAINER)
            item.put("event", KEY_EVENT)
            item.put("hits_time", timeStamp.toString())
            item
        } catch (e: JSONException) {
            JSONObject()
        }
    }


}