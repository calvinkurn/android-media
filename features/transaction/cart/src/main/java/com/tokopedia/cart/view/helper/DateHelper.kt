package com.tokopedia.cart.view.helper

import com.tokopedia.utils.date.DateUtil
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

object DateHelper {

    private const val FORMAT_YYYY_MM_DD_T_HH_MM_SS_ZZZZZ = "yyyy-MM-dd'T'HH:mm:ssZZZZZ"

    fun toDate(
        input: String,
        format: String = FORMAT_YYYY_MM_DD_T_HH_MM_SS_ZZZZZ
    ): Date? {
        return try {
            SimpleDateFormat(format, DateUtil.DEFAULT_LOCALE).parse(input)
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
    }
}
