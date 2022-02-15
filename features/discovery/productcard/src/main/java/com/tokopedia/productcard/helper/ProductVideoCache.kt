package com.tokopedia.productcard.helper

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

internal object ProductVideoCache {
    private var INSTANCE: Cache? = null

    private const val BYTES_IN_MB = 1024 * 1024
    private const val CACHE_SIZE_IN_MB = 25L

    private const val CACHE_FOLDER_NAME = "product_video"

    fun getInstance(context: Context): Cache = synchronized(this) {
        if (INSTANCE == null) {
            val cacheFolder = File(context.cacheDir, CACHE_FOLDER_NAME)
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(CACHE_SIZE_IN_MB * BYTES_IN_MB)
            val cacheDbProvider = ExoDatabaseProvider(context)

            INSTANCE = SimpleCache(cacheFolder, cacheEvictor, cacheDbProvider)
        }
        return@synchronized INSTANCE as Cache
    }

}