package com.tokopedia.shop.common.util

import java.net.URLEncoder

object UrlParamUtil {

    fun convertMapToStringParam(map: Map<String, String>): String{
        val paramList = mutableListOf<String>()
        for (entry in map.entries) {
            val paramKeyValue =  "${entry.key}=${URLEncoder.encode(entry.value, "UTF-8")}"
            paramList.add(paramKeyValue)
        }
        return paramList.joinToString("&")
    }

}