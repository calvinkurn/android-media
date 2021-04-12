package com.tokopedia.search.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

object UrlParamUtils {

    @JvmStatic
    fun getParamMap(paramString: String): HashMap<String?, String?> {
        if (paramString.isBlank()) return hashMapOf()

        val map = hashMapOf<String?, String?>()
        val params = paramString.split("&".toRegex())

        params.forEach { param ->
            val mapEntry = param.split("=".toRegex()).filter { it.isNotBlank() }
            if (mapEntry.size == 2) {
                val name = mapEntry[0]
                val value = mapEntry[1]
                map[name] = omitNewlineAndPlusSign(value)
            }
        }

        return map
    }

    private fun omitNewlineAndPlusSign(text: String): String {
        return text.replace("\n", "").replace("+", " ")
    }

    @JvmStatic
    fun <T> generateUrlParamString(paramMap: Map<String?, T?>?): String {
        if (paramMap == null || paramMap.isEmpty()) return ""

        val paramList = createParameterListFromMap(paramMap)
        return joinWithDelimiter("&", paramList)
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
        val queryParamsMap = getParamMap(queryParams)
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