package com.tokopedia.home.beranda.data.datasource.local.converter

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.tokopedia.home.beranda.domain.model.*
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_HOME_DATA_TO_STRING
import com.tokopedia.home.beranda.helper.benchmark.TRACE_STRING_TO_HOME_DATA
import java.util.*

class Converters {
    private val gson = GsonBuilder().serializeNulls().create()

    @TypeConverter
    fun convertHomeData(data: String): HomeData {
        BenchmarkHelper.beginSystraceSection(TRACE_STRING_TO_HOME_DATA)
        val output =  gson.fromJson(data, HomeData::class.java)
        BenchmarkHelper.endSystraceSection()
        return output
    }

    @TypeConverter
    fun convertHomeData(homeData: HomeData): String{
        BenchmarkHelper.beginSystraceSection(TRACE_HOME_DATA_TO_STRING)
        val output = gson.toJson(homeData)
        BenchmarkHelper.endSystraceSection()
        return output
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