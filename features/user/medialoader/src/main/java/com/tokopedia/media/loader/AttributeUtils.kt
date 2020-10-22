package com.tokopedia.media.loader

import android.content.Context
import java.sql.Timestamp
import java.util.Date

object AttributeUtils {

    private const val MEDIA_QUALITY_PREF = "media_image_quality"
    private const val KEY_QUALITY_SETTING = "index_image_quality_setting"

    fun getQualitySetting(context: Context): Int {
        return context.getSharedPreferences(MEDIA_QUALITY_PREF, Context.MODE_PRIVATE)
                .getInt(KEY_QUALITY_SETTING, 0)
    }

    fun getDateTime(): String {
        val stamp = Timestamp(System.currentTimeMillis())
        val date = Date(stamp.time)
        return date.toString()
    }
}