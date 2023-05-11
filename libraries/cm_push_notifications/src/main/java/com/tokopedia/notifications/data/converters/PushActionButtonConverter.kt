package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.ActionButton

class PushActionButtonConverter {

    @TypeConverter
    fun toActionButton(value: String?): List<ActionButton>? {
        if (value == null)
            return null
        val listType = object : TypeToken<List<ActionButton>>() {}.type
        return Gson().fromJson<List<ActionButton>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<ActionButton>?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  PushActionButtonConverter()
    }

}
