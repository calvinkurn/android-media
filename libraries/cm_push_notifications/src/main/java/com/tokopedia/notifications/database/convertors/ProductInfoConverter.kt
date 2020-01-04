package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.notifications.model.ProductInfo
import java.util.ArrayList

class ProductInfoConverter {
    @TypeConverter
    fun toProductInfo(value: String?): ArrayList<ProductInfo>? {
        if (value == null)
            return null
        val listType = object : TypeToken<ArrayList<ProductInfo>>() {}.type
        return Gson().fromJson<ArrayList<ProductInfo>>(value, listType)
    }

    @TypeConverter
    fun toJson(list: ArrayList<ProductInfo>?): String? {
        if (list == null)
            return null
        return Gson().toJson(list)
    }

    companion object{
        val instances =  ProductInfoConverter()
    }
}