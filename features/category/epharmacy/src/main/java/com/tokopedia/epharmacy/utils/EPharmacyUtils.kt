package com.tokopedia.epharmacy.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.usecase.BuildConfig
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object EPharmacyUtils {

    fun logException(e: Exception) {
        if (!BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(e)
        } else {
            e.printStackTrace()
        }
    }

    private fun formatDateToLocal(currentFormat: String = YYYY_MM_DD_T_HH_MM_SS_Z, newFormat: String = NEW_DATE_FORMAT, dateString: String): Date? {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
            fromFormat.isLenient = false
            fromFormat.timeZone = TimeZone.getTimeZone(UTC)
            val toFormat: DateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
            toFormat.isLenient = false
            toFormat.timeZone = TimeZone.getDefault()

            fromFormat.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun isOutsideWorkingHours(openTime : String , closeTime : String) : Boolean {
        val openTimeLocal : Date? = formatDateToLocal(dateString = openTime)
        val closeTimeLocal : Date? = formatDateToLocal(dateString = closeTime)
        val currentLocal = Calendar.getInstance().time
        return (openTimeLocal != null) && ((currentLocal < openTimeLocal) || (currentLocal > closeTimeLocal))
    }
}

enum class PrescriptionActionType(val type: String) {
    REDIRECT_PWA("REDIRECT_CONSULTATION_PWA"),
    REDIRECT_OPTION("SHOW_PRESCRIPTION_ATTACHMENT_OPTION"),
    REDIRECT_UPLOAD("REDIRECT_PRESCRIPTION_UPLOAD_PAGE"),
    REDIRECT_PRESCRIPTION("REDIRECT_CONSULTATION_PRESCRIPTION"),
    REDIRECT_NONE("NONE")
}
