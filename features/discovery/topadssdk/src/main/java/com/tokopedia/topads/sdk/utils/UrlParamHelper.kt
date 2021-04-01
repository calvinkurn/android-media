package com.tokopedia.topads.sdk.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

object UrlParamHelper {
    fun <T> generateUrlParamString(paramMap: Map<String, T>): String {
        if (paramMap.isEmpty()) {
            return ""
        }
        val paramList = createParameterListFromMap(paramMap)
        return joinWithDelimiter(tokens = paramList)
    }

    private fun <T> createParameterListFromMap(paramMap: Map<String, T>): List<String> {
        val paramList: MutableList<String> = ArrayList()
        for (entry in paramMap.entries) {
            if (mapEntryHasNulls(entry)) continue
            addParameterEntryToList(paramList, entry)
        }
        return paramList
    }

    private fun <T> mapEntryHasNulls(entry: Map.Entry<String, T>): Boolean {
        return entry.value == null
    }

    private fun <T> addParameterEntryToList(paramList: MutableList<String>, entry: Map.Entry<String, T>) {
        try {
            if (entry.value != null) {
                paramList.add(entry.key + "=" + URLEncoder.encode(entry.value.toString(), "UTF-8"))
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    private fun joinWithDelimiter(delimiter: CharSequence = "&", tokens: Iterable<*>): String {
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
}