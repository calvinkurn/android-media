package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import org.json.JSONException
import org.json.JSONObject

class JsonObjectConverter {

    @TypeConverter
    fun toJsonObject(jsonStr: String?): JSONObject? {
        return try {
            if (!jsonStr.isNullOrEmpty()) JSONObject(jsonStr) else null
        } catch (e: JSONException) {
            null
        }
    }

    @TypeConverter
    fun toJsonString(jsonObject: JSONObject?): String? {
        return jsonObject?.let { jsonObject.toString() }
    }

    companion object{
        val instances =  JsonObjectConverter()
    }
}