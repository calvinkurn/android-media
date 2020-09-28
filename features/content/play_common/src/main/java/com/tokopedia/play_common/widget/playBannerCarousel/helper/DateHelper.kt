package com.tokopedia.play_common.widget.playBannerCarousel.helper

import java.text.SimpleDateFormat
import java.util.*

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
