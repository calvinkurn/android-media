package com.tokopedia.topads.sdk.utils

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object TopAdsSdkUtil {

    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun isTimerValid(saleStartDate: Date?, saleEndDate: Date?): Boolean {
        if (saleStartDate == null || saleEndDate == null) return false
        val currentSystemTime = Calendar.getInstance().time
        return currentSystemTime.time > saleStartDate.time && currentSystemTime.time < saleEndDate.time
    }

    fun parseData(date: String?, timerFormat: String = DATE_FORMAT): Date? {
        return date?.let {
            try {
                SimpleDateFormat(timerFormat, Locale.getDefault())
                    .parse(date)
            } catch (parseException: ParseException) {
                null
            }
        }
    }

    fun isEnableTopAdsSdkV2(context: Context): Boolean {
        return try {
            val remoteConfig = FirebaseRemoteConfigImpl(context)
            remoteConfig.getBoolean(RemoteConfigKey.ANDROID_TOP_ADS_SDK_V2, true)
        } catch (e: Exception) {
            false
        }
    }
}
