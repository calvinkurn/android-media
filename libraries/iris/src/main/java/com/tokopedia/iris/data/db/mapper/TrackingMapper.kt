package com.tokopedia.iris.data.db.mapper

import com.tokopedia.iris.data.db.table.Tracking
import com.tokopedia.iris.util.KEY_CONTAINER
import com.tokopedia.iris.util.KEY_EVENT
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
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

        event.put(reformatEvent(track, sessionId))

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
        var event = JSONArray()
        for (i in tracking.indices) {
            val item = tracking[i]
            if (!item.event.isBlank() && (item.event.contains("event"))) {
                val eventObject = JSONObject(item.event)
                logCertainItems(eventObject)
                event.put(eventObject)
                val nextItem: Tracking? = try {
                    tracking[i+1]
                } catch (e: IndexOutOfBoundsException) {
                    null
                }
                val nextUserId : String = nextItem?.userId ?: ""
                if (item.userId != nextUserId || i == tracking.size - 1) {
                    val row = JSONObject()
                    row.put("device_id", item.deviceId)
                    row.put("user_id", item.userId)
                    if (event.length() > 0) {
                        row.put("event_data", event)
                        data.put(row)
                    }
                    event = JSONArray()
                }
            }
        }
        result.put("data", data)
        return result.toString()
    }

    private fun logCertainItems(item:JSONObject?){
        try {
            if (item?.has("event_ga") == true &&
                item.has("eventCategory") && item.has("eventAction")) {
                val itemEvent = item.getString("event_ga")
                val itemCat = item.getString("eventCategory")
                val itemAct = item.getString("eventAction")
                if ("clickTopNav" == itemEvent &&
                    itemCat?.startsWith("top nav") == true &&
                    "click search box" == itemAct) {
                    Timber.w("P1#IRIS_COLLECT#IRISSEND_CLICKSEARCHBOX")
                } else if ("clickPDP" == itemEvent && "product detail page" == itemCat &&
                    "click - tambah ke keranjang" == itemAct) {
                    Timber.w("P1#IRIS_COLLECT#IRISSEND_PDP_ATC")
                }
            }
        }catch (e:Exception) {
            Timber.e("P1#IRIS#logIrisAnalyticsSend %s", e.toString())
        }
    }

    companion object {

        fun reformatEvent(event: String, sessionId: String) : JSONObject {
            return try {
                val item = JSONObject(event)
                if (item.has("event") && item.get("event") != null) {
                    item.put("event_ga", item.get("event"))
                    item.remove("event")
                }
                item.put("iris_session_id", sessionId)
                item.put("container", KEY_CONTAINER)
                item.put("event", KEY_EVENT)
                item.put("hits_time", Calendar.getInstance().timeInMillis)
                item
            } catch (e: JSONException) {
                JSONObject()
            }
        }
    }
}