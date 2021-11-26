package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.PersistentButton
import java.util.*

class PushPersistentButtonConvertor {

    @TypeConverter
    fun toPersistentButton(value: String?): ArrayList<PersistentButton>? {
        if (value == null)
            return null
        val listType = object : TypeToken<ArrayList<PersistentButton>>() {}.type
        return Gson().fromJson<ArrayList<PersistentButton>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: ArrayList<PersistentButton>?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  PushPersistentButtonConvertor()
    }

}