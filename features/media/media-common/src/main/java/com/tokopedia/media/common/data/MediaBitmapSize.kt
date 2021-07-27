package com.tokopedia.media.common.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class MediaBitmapSize constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun saveSize(value: Double) {
        if (value == 0.0) return

        val currentSize = if (!getString(KEY_BITMAP_SIZE).isNullOrEmpty()) {
            getString(KEY_BITMAP_SIZE)
        } else {
            "0"
        }

        putString(KEY_BITMAP_SIZE, (currentSize.toLong() + value).toString().take(5))
    }

    fun getSize(): Int = getInt(KEY_BITMAP_SIZE)

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}