package com.tokopedia.media.common.data

import android.content.Context

class MediaBitmapSize(context: Context?) : MediaPreferences(context) {

    fun save(value: String) {
        val currentSize = getString(KEY_BITMAP_SIZE)
        insert(KEY_BITMAP_SIZE, (currentSize.toLong() + value.toLong()).toString())
    }

    fun size() = getInt(KEY_BITMAP_SIZE)

    companion object {
        private const val KEY_BITMAP_SIZE = "media_accumulative_bitmap_size"
    }

}