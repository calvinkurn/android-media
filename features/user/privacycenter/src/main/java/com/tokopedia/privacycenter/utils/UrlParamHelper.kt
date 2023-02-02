package com.tokopedia.privacycenter.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/*
* This function use to generate map uri param to string
* ex:
* paramMap = mapOf(
*   "device" to "android,
*   "source" to "search"
* )
*
* will convert to:
* device=android&source=searchbar
* */
fun <T> generateUrlParamString(paramMap: Map<String?, T>): String {
    if (mapIsEmpty(paramMap)) {
        return ""
    }
    val paramList = createParameterListFromMap(paramMap)
    return joinWithDelimiter(paramList)
}

private fun <T> mapIsEmpty(paramMap: Map<String?, T>?): Boolean {
    return paramMap == null || paramMap.isEmpty()
}

private fun <T> createParameterListFromMap(paramMap: Map<String?, T>): List<String> {
    val paramList: MutableList<String> = ArrayList()
    for (entry in paramMap.entries) {
        if (mapEntryHasNulls<T>(entry)) continue
        addParameterEntryToList<T>(paramList, entry)
    }
    return paramList
}

private fun <T> mapEntryHasNulls(entry: Map.Entry<String?, T?>): Boolean {
    return entry.key == null || entry.value == null
}

private fun <T> addParameterEntryToList(
    paramList: MutableList<String>,
    entry: Map.Entry<String?, T?>
) {
    try {
        if (entry.value != null) {
            paramList.add(entry.key + "=" + URLEncoder.encode(entry.value.toString(), "UTF-8"))
        }
    } catch (_: UnsupportedEncodingException) {}
}

private fun joinWithDelimiter(tokens: Iterable<*>): String {
    val it = tokens.iterator()
    if (!it.hasNext()) {
        return ""
    }
    val sb = StringBuilder()
    sb.append(it.next())
    while (it.hasNext()) {
        sb.append("&")
        sb.append(it.next())
    }
    return sb.toString()
}
