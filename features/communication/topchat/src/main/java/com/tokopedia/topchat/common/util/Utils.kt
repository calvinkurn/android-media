package com.tokopedia.topchat.common.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ"
    private const val LANGUAGE_CODE = "in"
    private const val COUNTRY_CODE = "ID"
    private var mLocale: Locale? = null

    fun getDateTime(isoTime: String?): String {
        return try {
            val inputFormat = SimpleDateFormat(DATE_FORMAT, locale)
            val dateFormat = SimpleDateFormat("dd MMMM yyyy", locale)
            val date = inputFormat.parse(isoTime)
            dateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            e.localizedMessage
        } catch (e: Exception) {
            e.printStackTrace()
            e.localizedMessage
        }
    }

    val locale: Locale?
        get() {
            if (mLocale == null) mLocale = Locale(LANGUAGE_CODE, COUNTRY_CODE, "")
            return mLocale
        }
}