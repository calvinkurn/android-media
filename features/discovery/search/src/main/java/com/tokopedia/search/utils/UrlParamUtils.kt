package com.tokopedia.search.utils

import com.tokopedia.utils.text.currency.StringUtils.isNotBlank
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

object UrlParamUtils {
    @JvmStatic
    fun getParamMap(paramString: String): HashMap<String?, String> {
        val map = HashMap<String?, String>()
        if (isNotBlank(paramString)) {
            val params = paramString.split("&".toRegex()).toTypedArray()
            for (param in params) {
                val data = param.split("=".toRegex()).toTypedArray()
                if (data.size == 2) {
                    val name = data[0]
                    val value = data[1]
                    map[name] = omitNewlineAndPlusSign(value)
                }
            }
        }
        return map
    }

    private fun omitNewlineAndPlusSign(text: String): String {
        return text.replace("\n", "").replace("+", " ")
    }

    @JvmStatic
    fun <T> generateUrlParamString(paramMap: Map<String?, T?>): String {
        if (mapIsEmpty(paramMap)) {
            return ""
        }
        val paramList = createParameterListFromMap(paramMap)
        return joinWithDelimiter("&", paramList)
    }

    private fun <T> mapIsEmpty(paramMap: Map<String?, T?>?): Boolean {
        return paramMap == null || paramMap.isEmpty()
    }

    private fun <T> createParameterListFromMap(paramMap: Map<String?, T?>): List<String> {
        val paramList: MutableList<String> = ArrayList()
        for (entry in paramMap.entries) {
            if (mapEntryHasNulls(entry)) continue
            addParameterEntryToList(paramList, entry)
        }
        return paramList
    }

    private fun <T> mapEntryHasNulls(entry: Map.Entry<String?, T?>): Boolean {
        return entry.key == null || entry.value == null
    }

    private fun <T> addParameterEntryToList(paramList: MutableList<String>, entry: Map.Entry<String?, T?>) {
        try {
            paramList.add(entry.key.toString() + "=" + URLEncoder.encode(entry.value.toString(), "UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private fun joinWithDelimiter(delimiter: CharSequence, tokens: Iterable<*>): String {
        val it = tokens.iterator()
        if (!it.hasNext()) {
            return ""
        }
        val sb = StringBuilder()
        sb.append(it.next())
        while (it.hasNext()) {
            sb.append(delimiter)
            sb.append(it.next())
        }
        return sb.toString()
    }

    @JvmStatic
    fun removeKeysFromQueryParams(queryParams: String?, keysToRemove: List<String?>?): String {
        if (queryParams == null) return ""
        if (keysToRemove == null || keysToRemove.isEmpty()) return queryParams
        val queryParamsMap: MutableMap<String?, String> = getParamMap(queryParams)
        for (key in keysToRemove) queryParamsMap.remove(key)
        return generateUrlParamString(queryParamsMap)
    }

    @JvmStatic
    fun getQueryParams(url: String?): String {
        if (url == null) return ""
        val splitUrl = url.split("\\?".toRegex()).toTypedArray()
        return if (splitUrl.size < 2) "" else splitUrl[1]
    }
}