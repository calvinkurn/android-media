package com.tokopedia.notifications.data.converters

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.model.PushPayloadExtra

class PushPayloadExtraConverter {

    @TypeConverter
    fun toPayloadExtra(value: String?): PushPayloadExtra? {
        if (value == null)
            return null
        return try {
            val listType = object : TypeToken<PushPayloadExtra>() {}.type
            Gson().fromJson<PushPayloadExtra>(value, listType)
        } catch (e: Exception){
            null
        }
    }

    @TypeConverter
    fun toJson(list: PushPayloadExtra?): String? {
        if (list == null)
            return null
        return try {
            val gson = Gson()
            return gson.toJson(list)
        } catch (e: Exception) {
            null
        }

    }

    companion object{
        val instances =  PushPayloadExtraConverter()
    }
}