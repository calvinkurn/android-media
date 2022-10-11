package com.tokopedia.topchat.common

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.AndroidFileUtil
import java.io.StringReader
import java.util.*


inline fun <reified T> alterResponseOf(
    responsePath: String,
    altercation: (JsonObject) -> Unit
): T {
    val responseObj: JsonObject = AndroidFileUtil.parse(
        responsePath, JsonObject::class.java
    )
    altercation(responseObj)
    return CommonUtil.fromJson(
        responseObj.toString(), T::class.java
    )
}

inline fun <reified T> fromJson(json: String): T {
    val reader = StringReader(json)
    return Gson().fromJson(reader, T::class.java)
}

/**
 * return next week timestamp in seconds
 */
fun getNextWeekTimestamp(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    return calendar.timeInMillis / 1_000
}

/**
 * return the next 6 hours timestamp from now in seconds
 */
fun getNext6Hours(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR_OF_DAY, 6)
    return calendar.timeInMillis / 1_000
}

/**
 * return the next 1 seconds timestamp from now in seconds
 */
fun getNext3Seconds(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.SECOND, 3)
    return calendar.timeInMillis / 1_000
}