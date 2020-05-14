package com.tokopedia.analyticsdebugger.debugger.data.mapper

import com.tokopedia.config.GlobalConfig
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TetraMapper {

    var deviceType: String = "android"
    var deviceId: String = GlobalConfig.DEVICE_ID

    fun parseInitRequest(): String {
        val request = JSONObject()
        request.put("deviceType", deviceType)
        request.put("deviceId", deviceId)
        return request.toString()
    }

    fun parseInitResponse(response: String?): Boolean {
        try {
            val result = JSONObject(response)
            return result.optBoolean("isWhitelisted")
        } catch (exception: JSONException) {}
        return false
    }

    fun parseDebugRequest(userId: String, event: Map<String, Any>): String {
        val data = JSONObject(event).toString()
        val timestamp = SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z",
                Locale.getDefault()).format(Date())

        val request = JSONObject()
        request.put("deviceType", deviceType)
        request.put("deviceId", deviceId)
        request.put("userId", userId)
        request.put("timestamp", timestamp)
        request.put("data", data)
        return request.toString()
    }

    fun parseDebugResponse(response: String?) {
        response?.let {
            Timber.d(it)
        }
    }
}