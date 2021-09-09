package com.tokopedia.play.widget.player

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

/**
 * Created by jegul on 19/08/21
 */
class PlayWidgetVideoCache {

    companion object {
        private var INSTANCE: Cache? = null

        private const val BYTES_IN_MB = 1024 * 1024
        private const val CACHE_SIZE_IN_MB = 25L

        private const val CACHE_FOLDER_NAME = "play_widget"

        fun getInstance(context: Context): Cache = synchronized(this) {
            if (INSTANCE == null) {
                val cacheFolder = File(context.cacheDir, CACHE_FOLDER_NAME)
                val cacheEvictor = LeastRecentlyUsedCacheEvictor(CACHE_SIZE_IN_MB * BYTES_IN_MB)
                val cacheDbProvider = ExoDatabaseProvider(context)

                INSTANCE = SimpleCache(cacheFolder, cacheEvictor, cacheDbProvider)
            }
            return@synchronized INSTANCE!!
        }
    }
}