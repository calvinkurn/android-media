package com.tokopedia.shop.home.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun getDateFromString(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }

}