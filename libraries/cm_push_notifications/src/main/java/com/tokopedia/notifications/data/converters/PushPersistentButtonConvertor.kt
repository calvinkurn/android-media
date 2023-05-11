package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.PersistentButton

class PushPersistentButtonConvertor {

    @TypeConverter
    fun toPersistentButton(value: String?): List<PersistentButton>? {
        if (value == null)
            return null
        val listType = object : TypeToken<List<PersistentButton>>() {}.type
        return Gson().fromJson<List<PersistentButton>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<PersistentButton>?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  PushPersistentButtonConvertor()
    }

}
