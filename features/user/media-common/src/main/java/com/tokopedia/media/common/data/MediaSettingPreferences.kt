package com.tokopedia.media.common.data

import android.content.Context
import android.content.SharedPreferences

class MediaSettingPreferences constructor(val context: Context?) {

    private fun pref(): SharedPreferences? {
        return context?.getSharedPreferences(MEDIA_QUALITY_PREF, Context.MODE_PRIVATE)
    }

    fun qualitySettings(): Int {
        return if (isExist(KEY_QUALITY_SETTING)) pref()?.getInt(KEY_QUALITY_SETTING, 0)?: 0 else 0
    }

    fun toasterVisibility(): Boolean {
        return if (isExist(KEY_MEDIA_TOASTER)) pref()?.getBoolean(KEY_MEDIA_TOASTER, false)?: false else false
    }

    fun setQualitySettings(value: Int) {
        pref()?.edit()?.putInt(KEY_QUALITY_SETTING, value)?.apply()
    }

    fun setToasterVisibilityFlag(value: Boolean) {
        pref()?.edit()?.putBoolean(KEY_MEDIA_TOASTER, value)?.apply()
    }

    private fun isExist(key: String): Boolean {
        return pref()?.contains(key)?: false
    }

    fun getQualitySetting(index: Int): String {
        return when(index) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }

    companion object {
        private const val MEDIA_QUALITY_PREF = "media_image_quality"
        private const val KEY_QUALITY_SETTING = "index_image_quality_setting"
        private const val KEY_MEDIA_TOASTER = "index_media_toaster_visibility"
    }

}