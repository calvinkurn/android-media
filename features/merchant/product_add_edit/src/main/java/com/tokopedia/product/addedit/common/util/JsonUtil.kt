package com.tokopedia.product.addedit.common.util

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.network.CacheUtil

object JsonUtil {
    fun <T> mapObjectToJson(item: T?): String? {
        return if (item != null) {
            CacheUtil.convertModelToString(item, object : TypeToken<T>() {}.type)
        } else {
            null
        }
    }

    fun <T> mapJsonToObject(jsonData : String, itemClass: Class<T>): T {
        return CacheUtil.convertStringToModel(jsonData, itemClass)
    }
}
