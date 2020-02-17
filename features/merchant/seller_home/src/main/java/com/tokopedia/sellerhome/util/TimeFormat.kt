package com.tokopedia.sellerhome.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 2020-01-30
 */

object TimeFormat {

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(time: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(time)
    }
}