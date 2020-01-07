package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.Grid
import java.util.ArrayList

class GridConverter {

    @TypeConverter
    fun toGrid(value: String?): ArrayList<Grid>? {
        if (value == null)
            return null
        val listType = object : TypeToken<ArrayList<Grid>>() {}.type
        return Gson().fromJson<ArrayList<Grid>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: ArrayList<Grid>?): String? {
        if (list == null)
            return null
        return Gson().toJson(list)
    }

    companion object{
        val instances =  GridConverter()
    }
}