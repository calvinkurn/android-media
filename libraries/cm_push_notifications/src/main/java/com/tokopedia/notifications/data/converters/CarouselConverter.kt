package com.tokopedia.notifications.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.Carousel

class CarouselConverter {

    @TypeConverter
    fun toCarousel(value: String?): List<Carousel>? {
        if (value == null)
            return null
        val listType = object : TypeToken<List<Carousel>>() {}.type
        return Gson().fromJson<List<Carousel>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: List<Carousel>?): String? {
        if (list == null)
            return null
        return Gson().toJson(list)
    }

    companion object{
        val instances =  CarouselConverter()
    }
}
