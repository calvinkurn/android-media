package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.PayloadExtra
import java.util.ArrayList

class ReviewConverter {

    @TypeConverter
    fun toPayloadExtra(value: String?): PayloadExtra? {
        if (value == null)
            return null
        val listType = object : TypeToken<PayloadExtra>() {}.type
        return Gson().fromJson<PayloadExtra>(value, listType)
    }

    @TypeConverter
    fun toJson(list: PayloadExtra?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  ReviewConverter()
    }
}