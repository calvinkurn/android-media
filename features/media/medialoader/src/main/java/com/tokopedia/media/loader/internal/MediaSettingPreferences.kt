package com.tokopedia.media.loader.internal

import android.content.Context
import androidx.compose.ui.res.painterResource
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.media.loader.data.MEDIA_QUALITY_PREF

class MediaSettingPreferences constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun qualitySettings(): Int {
        return getInt(KEY_QUALITY_SETTING)?: 0
    }

    fun toasterVisibility(): Boolean {
        return getBoolean(KEY_MEDIA_TOASTER)?: false
    }

    fun glideMigration(): Boolean {
        return getBoolean(KEY_GLIDE_CLEAR_CACHE)?: false
    }

    fun setQualitySettings(value: Int) {
        putInt(KEY_QUALITY_SETTING, value)
        applyEditor()
    }

    fun hasShowToaster(value: Boolean) {
        putBoolean(KEY_MEDIA_TOASTER, value)
        applyEditor()
    }

    fun hasGlideMigration(value: Boolean) {
        putBoolean(KEY_GLIDE_CLEAR_CACHE, value)
        applyEditor()
    }

    companion object {
        private const val KEY_QUALITY_SETTING = "index_image_quality_setting"
        private const val KEY_MEDIA_TOASTER = "index_media_toaster_visibility"
        private const val KEY_GLIDE_CLEAR_CACHE = "medialoader_clear_disk_cache"

        @Volatile var preferences: MediaSettingPreferences? = null

        fun instance(context: Context): MediaSettingPreferences {
            return synchronized(this) {
                preferences ?: MediaSettingPreferences(context).also {
                    preferences = it
                }
            }
        }
    }

}
