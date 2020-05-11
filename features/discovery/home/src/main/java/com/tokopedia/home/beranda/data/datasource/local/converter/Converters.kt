package com.tokopedia.home.beranda.data.datasource.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.domain.model.banner.BannerDataModel
import java.util.*


class Converters {
    private val gson = Gson()

    @TypeConverter
    fun convertHomeData(data: String): HomeData {
        return gson.fromJson(data, HomeData::class.java)
    }

    @TypeConverter
    fun convertHomeData(homeData: HomeData): String{
        return gson.toJson(homeData)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?) : Date? {
        return if(value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if(date == null) null else date.time
    }
}