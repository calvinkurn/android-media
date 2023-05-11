package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.Grid

class GridConverter {

    @TypeConverter
    fun toGrid(value: String?): List<Grid>? {
        if (value == null)
            return null
        val listType = object : TypeToken<List<Grid>>() {}.type
        return Gson().fromJson<List<Grid>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Grid>?): String? {
        if (list == null)
            return null
        return Gson().toJson(list)
    }

    companion object{
        val instances =  GridConverter()
    }
}
