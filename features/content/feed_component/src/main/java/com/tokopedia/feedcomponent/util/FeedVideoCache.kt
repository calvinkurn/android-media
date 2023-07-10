package com.tokopedia.feedcomponent.util

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.Downloader
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper
import com.google.android.exoplayer2.offline.StreamKey
import com.google.android.exoplayer2.source.hls.offline.HlsDownloader
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheUtil
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.tokopedia.content.common.util.Bytes
import com.tokopedia.content.common.util.fromMegaBytes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by kenny.hadisaputra on 06/07/23
 */
class FeedVideoCache private constructor(
    val cache: Cache
) {

    private val bytesToCache = Bytes.fromMegaBytes(MB_TO_CACHE)

    private val commonProgressListener = CacheUtil.ProgressListener { _, _, _ ->
    }

    private val ongoingDownloaderQueue = ConcurrentHashMap<String, Downloader>()

    private val mutex = Mutex()

    private suspend fun cache(context: Context, uri: Uri) = coroutineScope {
        val type = Util.inferContentType(uri)
        if (type == C.TYPE_HLS) {
            cacheHls(context, uri, bytesToCache)
        } else {
            cacheMedia(context, uri, bytesToCache)
        }
    }

    suspend fun cache(context: Context, url: String) = cache(context, Uri.parse(url))

    fun isCached(uri: Uri): Boolean {
        val type = Util.inferContentType(uri)
        return if (type == C.TYPE_HLS) {
            isHlsCached(uri)
        } else {
            isMediaCached(uri)
        }
    }

    fun isCached(url: String): Boolean {
        return isCached(Uri.parse(url))
    }

    private fun cancel() {
        ongoingDownloaderQueue.values.forEach { it.cancel() }
    }

    private fun release() {
        cache.release()
    }

    private fun cacheMedia(context: Context, uri: Uri, bytesToCache: Bytes) {
        try {
            val dataSpec = createDataSpec(uri, bytesToCache)
            val dataSource = FeedExoUtil.getDataSourceFactory(context).createDataSource()
            CacheUtil.cache(dataSpec, cache, dataSource, commonProgressListener, null)
        } catch (_: Exception) { }
    }

    private suspend fun cacheHls(context: Context, uri: Uri, bytesToCache: Bytes) {
        val downloader = HlsDownloader(
            uri,
            Collections.singletonList(
                StreamKey(HlsMasterPlaylist.GROUP_INDEX_VARIANT, 0)
            ),
            DownloaderConstructorHelper(cache, FeedExoUtil.getDataSourceFactory(context))
        )

        try {
            val listener = Downloader.ProgressListener { contentLength, bytesDownloaded, percentDownloaded ->
                if (bytesDownloaded >= bytesToCache.length) {
                    downloader.cancel()
                }
            }

            mutex.withLock {
                if (ongoingDownloaderQueue.containsKey(uri.toString())) return
                ongoingDownloaderQueue[uri.toString()] = downloader
            }
            downloader.download(listener)
        } catch (_: InterruptedException) {
        } catch (_: Exception) {
        } finally {
            ongoingDownloaderQueue.remove(uri.toString())
        }
    }

    private fun isHlsCached(uri: Uri): Boolean {
        return cache.isCached(uri.toString(), 0, MB_TO_CACHE)
    }

    private fun isMediaCached(uri: Uri): Boolean {
        val dataSpec = createDataSpec(uri, bytesToCache)
        val cached = CacheUtil.getCached(dataSpec, cache, null)
        return cached.second > 0L
    }

    private fun createDataSpec(uri: Uri, bytesToCache: Bytes): DataSpec {
        return DataSpec(uri, 0, bytesToCache.length, null)
    }

    companion object {

        @Volatile
        private var INSTANCE: FeedVideoCache? = null

        private const val DIR_NAME = "feed_video"

        private const val MB_TO_CACHE = 2L

        fun getInstance(context: Context): FeedVideoCache =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: createInstance(context).also {
                    INSTANCE = it
                }
            }

        fun cleanUp(context: Context) {
            if (INSTANCE == null) return
            val instance = getInstance(context)
            instance.cancel()
            instance.release()
            deleteCacheDir(context)
            synchronized(this) { INSTANCE = null }
        }

        private fun createInstance(context: Context): FeedVideoCache {
            deleteCacheDir(context)

            val cacheDir = getCacheDir(context)
            val cache = SimpleCache(
                cacheDir,
                NoOpCacheEvictor(),
                ExoDatabaseProvider(context.applicationContext)
            )

            return FeedVideoCache(cache)
        }

        private fun getCacheDir(context: Context): File {
            return File(context.externalCacheDir, DIR_NAME)
        }

        private fun deleteCacheDir(context: Context) {
            val cacheDir = getCacheDir(context)
            try {
                cacheDir.deleteRecursively()
            } catch (_: Exception) { }
        }
    }
}
