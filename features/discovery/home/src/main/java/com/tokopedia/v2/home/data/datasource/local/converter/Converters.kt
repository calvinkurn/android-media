package com.tokopedia.v2.home.data.datasource.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.tokopedia.v2.home.model.pojo.home.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun convertDynamicHomeChannel(data: String): DynamicHomeChannel {
        return gson.fromJson(data, DynamicHomeChannel::class.java)
    }

    @TypeConverter
    fun convertDynamicHomeChannel(dynamicHomeChannel: DynamicHomeChannel): String{
        return gson.toJson(dynamicHomeChannel)
    }

    @TypeConverter
    fun convertBannerDataModel(data: String): Banner {
        return gson.fromJson(data, Banner::class.java)
    }

    @TypeConverter
    fun convertBannerDataModel(bannerDataModel: Banner): String{
        return gson.toJson(bannerDataModel)
    }

    @TypeConverter
    fun convertDynamicHomeIcon(data: String): DynamicHomeIcon {
        return gson.fromJson(data, DynamicHomeIcon::class.java)
    }

    @TypeConverter
    fun convertDynamicHomeIcon(dynamicHomeIcon: DynamicHomeIcon): String {
        return gson.toJson(dynamicHomeIcon)
    }

    @TypeConverter
    fun convertHomeFlag(data: String): HomeFlag {
        return gson.fromJson(data, HomeFlag::class.java)
    }

    @TypeConverter
    fun convertHomeFlag(homeFlag: HomeFlag): String{
        return gson.toJson(homeFlag)
    }


    @TypeConverter
    fun convertSpotlight(data: String): Spotlight {
        return gson.fromJson(data, Spotlight::class.java)
    }

    @TypeConverter
    fun convertSpotlight(spotlight: Spotlight): String{
        return gson.toJson(spotlight)
    }

    @TypeConverter
    fun convertTicker(data: String): Ticker {
        return gson.fromJson(data, Ticker::class.java)
    }

    @TypeConverter
    fun convertSpotlight(ticker: Ticker): String{
        return gson.toJson(ticker)
    }
}