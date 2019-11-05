package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.Carousel
import java.util.ArrayList

class CarouselConverter {

    @TypeConverter
    fun toCarousel(value: String?): ArrayList<Carousel>? {
        if (value == null)
            return null
        val listType = object : TypeToken<ArrayList<Carousel>>() {}.type
        return Gson().fromJson<ArrayList<Carousel>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: ArrayList<Carousel>?): String? {
        if (list == null)
            return null
        return Gson().toJson(list)
    }

    companion object{
        val instances =  CarouselConverter()
    }
}