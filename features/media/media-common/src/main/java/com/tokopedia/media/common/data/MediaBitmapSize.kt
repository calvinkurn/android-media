package com.tokopedia.media.common.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class MediaBitmapSize constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun saveSize(value: Double) {
        if (value <= 0.0) return

        // get existing an accumulative bitmap size
        val getCurrentSize = getString(KEY_BITMAP_SIZE)
        val currentSize = if (!getCurrentSize.isNullOrEmpty()) getCurrentSize else "0"

        // entry a new value
        putString(KEY_BITMAP_SIZE, (currentSize.toLong() + value).toString().take(5))
        applyEditor()
    }

    fun getSize(): String = getString(KEY_BITMAP_SIZE)

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}