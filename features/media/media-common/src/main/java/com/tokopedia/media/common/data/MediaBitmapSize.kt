package com.tokopedia.media.common.data

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler

class MediaBitmapSize constructor(
    context: Context?
) : LocalCacheHandler(context, MEDIA_QUALITY_PREF) {

    fun saveSize(value: Long) {
        if (value == 0L) return

        val currentSize = getString(KEY_BITMAP_SIZE)?: "0"
        putString(KEY_BITMAP_SIZE, (currentSize.toLong() + value).toString())
    }

    fun getSize(): Int = getInt(KEY_BITMAP_SIZE)

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}