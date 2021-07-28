package com.tokopedia.media.common.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class MediaBitmapSize constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun saveSize(value: Double) {
        if (value <= 0.0) return

        val accumulativeSize = getSize().toDouble() + value
        putString(KEY_BITMAP_SIZE, accumulativeSize.toString().take(7))
        applyEditor()
    }

    fun getSize(): String {
        val getCurrentSize = getString(KEY_BITMAP_SIZE)
        return if (!getCurrentSize.isNullOrEmpty()) getCurrentSize else "0"
    }

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}