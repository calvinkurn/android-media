package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tokopedia.notifications.model.PayloadExtra
import org.json.JSONException

class PayloadExtraConverter {
    @TypeConverter
    fun toJsonObject(value: String?): PayloadExtra? {
        return try {
            if (value.isNullOrBlank())
                Gson().fromJson(value, PayloadExtra::class.java)
             else null
        } catch (e: JSONException) {
            null
        }
    }

    @TypeConverter
    fun toJsonString(value: PayloadExtra?): String? {
        return if (value == null) "" else Gson().toJson(value)
    }

    companion object {
        val instances = JsonObjectConverter()
    }
}