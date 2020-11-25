package com.tokopedia.home_component.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DevAra on 28/04/20.
 */
object DateHelper {
    fun getExpiredTime(expiredTimeString: String?): Date {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ")
        return try {
            format.parse(expiredTimeString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

    fun isExpired(serverTimeOffset: Long, expiredTime: Date?): Boolean {
        val serverTime = Date(System.currentTimeMillis())
        serverTime.time = serverTime.time + serverTimeOffset
        return serverTime.after(expiredTime)
    }
}