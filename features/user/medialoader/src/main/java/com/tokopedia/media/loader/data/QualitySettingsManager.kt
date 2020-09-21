package com.tokopedia.media.loader.data

import android.content.Context
import android.content.SharedPreferences

class QualitySettingsManager constructor(val context: Context) {

    private fun pref(): SharedPreferences {
        return context.getSharedPreferences(MEDIA_QUALITY_PREF, Context.MODE_PRIVATE)
    }

    fun qualitySettings(): Int {
        // default: adaptive image.
        return if (isExist()) pref().getInt(KEY_QUALITY_SETTING, 0) else 0
    }

    private fun isExist(): Boolean {
        return pref().contains(KEY_QUALITY_SETTING)
    }

    companion object {
        private const val MEDIA_QUALITY_PREF = "media_image_quality"
        private const val KEY_QUALITY_SETTING = "index_image_quality_setting"
    }

}