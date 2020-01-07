package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.ActionButton
import java.util.*

class PushActionButtonConverter {

    @TypeConverter
    fun toActionButton(value: String?): ArrayList<ActionButton>? {
        if (value == null)
            return null
        val listType = object : TypeToken<ArrayList<ActionButton>>() {}.type
        return Gson().fromJson<ArrayList<ActionButton>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: ArrayList<ActionButton>?): String? {
        if (list == null)
            return null
        val gson = Gson()
        return gson.toJson(list)
    }

    companion object{
        val instances =  PushActionButtonConverter()
    }

}