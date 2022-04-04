package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.PushPayloadExtra

class PushPayloadExtraConverter {

    @TypeConverter
    fun toPayloadExtra(value: String?): PushPayloadExtra? {
        if (value == null)
            return null
        val listType = object : TypeToken<PushPayloadExtra>() {}.type
        return Gson().fromJson<PushPayloadExtra>(value, listType)
    }

    @TypeConverter
    fun toJson(list: PushPayloadExtra?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  PushPayloadExtraConverter()
    }
}