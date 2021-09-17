package com.tokopedia.media.common.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class MediaBitmapSize constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun saveSize(value: String) {
        if (value.isEmpty() || value.toInt() == 0) return

        val accumulativeSize = getSize() + value.toInt()
        putLong(KEY_BITMAP_SIZE, accumulativeSize)
        applyEditor()
    }

    fun getSize(): Long {
        val getCurrentSize = getLong(KEY_BITMAP_SIZE)
        return if (getCurrentSize != 0L) getCurrentSize else 0
    }

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}