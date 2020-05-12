package com.tokopedia.vouchercreation.common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 12/05/20
 */

object DateTimeUtils {

    fun reformatDateTime(dateTime: String, oldFormat: String, newFormat: String): String {
        val locale = Locale.getDefault()
        val oldSdf = SimpleDateFormat(oldFormat, locale)
        val newSdf = SimpleDateFormat(newFormat, locale)
        return newSdf.format(oldSdf.parse(dateTime))
    }
}