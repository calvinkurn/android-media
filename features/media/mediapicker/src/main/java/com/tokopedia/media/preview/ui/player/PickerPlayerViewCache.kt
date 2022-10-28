package com.tokopedia.media.preview.ui.player

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

object PickerPlayerViewCache {

    private var instance: Cache? = null

    private const val BYTE_IN_MB = 1024 * 1024
    private const val CACHE_SIZE_IN_MB = 25L

    private const val CACHE_DIR_NAME = "media_picker_player"

    fun get(context: Context): Cache = synchronized(this) {
        return instance?: SimpleCache(
            File(context.cacheDir, CACHE_DIR_NAME),
            LeastRecentlyUsedCacheEvictor(CACHE_SIZE_IN_MB * BYTE_IN_MB),
            ExoDatabaseProvider(context)
        ).also {
            instance = it
        }
    }

}