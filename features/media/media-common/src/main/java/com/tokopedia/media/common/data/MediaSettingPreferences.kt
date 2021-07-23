package com.tokopedia.media.common.data

import android.content.Context
import android.content.SharedPreferences

class MediaSettingPreferences(context: Context?) : MediaPreferences(context) {

    fun qualitySettings(): Int {
        return if (isExist(KEY_QUALITY_SETTING)) {
            getInt(KEY_QUALITY_SETTING)
        } else {
            0
        }
    }

    fun toasterVisibility(): Boolean {
        return if (isExist(KEY_MEDIA_TOASTER)) {
            getBoolean(KEY_MEDIA_TOASTER)
        } else {
            false
        }
    }

    fun glideMigration(): Boolean {
        return if (isExist(KEY_GLIDE_CLEAR_CACHE)) {
            getBoolean(KEY_GLIDE_CLEAR_CACHE)
        } else {
            false
        }
    }

    fun setQualitySettings(value: Int) {
        insert(KEY_QUALITY_SETTING, value)
    }

    fun setToasterVisibilityFlag(value: Boolean) {
        insert(KEY_MEDIA_TOASTER, value)
    }

    fun setGlideMigration(value: Boolean) {
        insert(KEY_GLIDE_CLEAR_CACHE, value)
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
        private const val KEY_QUALITY_SETTING = "index_image_quality_setting"
        private const val KEY_MEDIA_TOASTER = "index_media_toaster_visibility"
        private const val KEY_GLIDE_CLEAR_CACHE = "medialoader_clear_disk_cache"
    }

}