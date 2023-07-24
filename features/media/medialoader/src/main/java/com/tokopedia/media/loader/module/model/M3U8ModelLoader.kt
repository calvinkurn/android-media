package com.tokopedia.media.loader.module.model

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylist
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistParser
import com.tokopedia.network.utils.ErrorHandler
import java.net.HttpURLConnection
import java.net.URL

private const val SCHEME_HTTPS = "https"
private const val M3U8_EXTENSION = ".m3u8"
private const val M3U8_VIDEO_ID_QUERY_PARAM = "id"
private const val M3U8_SEGMENT_URL_FORMAT = "%s://%s/%s/%s"
private const val M3U8_UNABLE_FIND_SEGMENT_ERROR_FORMAT = "Unable to find first video segment URL for video with URL: %s"

class M3U8ModelLoader : ModelLoader<String, Bitmap> {

    override fun buildLoadData(
        model: String,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> {
        return ModelLoader.LoadData(createSourceKey(model), M3U8DataFetcher(model))
    }

    override fun handles(model: String): Boolean {
        return try {
            model.isM3u8() && model.hasVideoID()
        } catch (e: Exception) {
            ErrorHandler.getErrorMessage(null, M3U8LoaderException().apply { initCause(e) })
            false
        }
    }

    private fun createSourceKey(model: String): Key {
        return ObjectKey(model)
    }

    private fun String.hasVideoID(): Boolean {
        return getVideoID().isNotBlank()
    }

    private fun String.isM3u8(): Boolean {
        return Uri.parse(this).lastPathSegment?.endsWith(M3U8_EXTENSION) == true
    }
}

class M3U8DataFetcher(private val masterPlaylistUrl: String) : DataFetcher<Bitmap> {

    companion object {
        private const val REQUEST_METHOD_GET = "GET"
    }

    private var isCancelled: Boolean = false
    private var connection: HttpURLConnection? = null
    private var retriever: MediaMetadataRetriever? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        try {
            val firstSegmentUrl = findFirstSegmentUrl()
            if (!isCancelled && firstSegmentUrl.isBlank()) {
                throw GlideException(
                    String.format(M3U8_UNABLE_FIND_SEGMENT_ERROR_FORMAT, masterPlaylistUrl)
                )
            } else {
                val bitmap = getFirstFrameBitmap(firstSegmentUrl)
                callback.onDataReady(bitmap)
            }
        } catch (e: Exception) {
            callback.onLoadFailed(M3U8LoaderException().apply { initCause(e) })
        }
    }

    override fun cleanup() {}

    override fun cancel() {
        isCancelled = true
        try { retriever?.release() } catch (_: Throwable) {}
    }

    override fun getDataClass(): Class<Bitmap> {
        return Bitmap::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    private fun findFirstSegmentUrl(url: String = masterPlaylistUrl): String {
        if (isCancelled) {
            return ""
        }

        setupConnection(url)
        return processHlsPlaylist(loadHlsPlaylist(url))
    }

    private fun setupConnection(url: String) {
        connection = URL(url).openConnection() as? HttpURLConnection
        connection?.requestMethod = REQUEST_METHOD_GET
    }

    private fun loadHlsPlaylist(url: String): HlsPlaylist? {
        val hlsPlaylist = connection?.inputStream?.buffered()?.use {
            HlsPlaylistParser().parse(Uri.parse(url), it)
        }
        connection?.disconnect()
        return hlsPlaylist
    }

    private fun processHlsPlaylist(hlsPlaylist: HlsPlaylist?): String {
        return when (hlsPlaylist) {
            is HlsMasterPlaylist -> {
                hlsPlaylist.mediaPlaylistUrls.firstOrNull()?.let { firstMediaPlaylist ->
                    findFirstSegmentUrl(firstMediaPlaylist.toString())
                }.orEmpty()
            }
            is HlsMediaPlaylist -> {
                hlsPlaylist.segments.firstOrNull()?.let { firstSegment ->
                    composeSegmentUrl(firstSegment, hlsPlaylist)
                }.orEmpty()
            }
            else -> ""
        }
    }

    private fun getFirstFrameBitmap(url: String): Bitmap? {
        if (isCancelled) {
            return null
        }

        retriever = MediaMetadataRetriever()
        return try {
            retriever?.setDataSource(url, mutableMapOf())
            retriever?.getFrameAtTime(0L, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (t: Throwable) {
            ErrorHandler.getErrorMessage(null, M3U8LoaderException().apply { initCause(t) })
            null
        } finally {
            retriever?.release()
        }
    }

    private fun composeSegmentUrl(segment: HlsMediaPlaylist.Segment, hlsPlaylist: HlsPlaylist): String {
        return if (segment.url.startsWith(SCHEME_HTTPS)) {
            segment.url
        } else {
            val uri = Uri.parse(hlsPlaylist.baseUri)
            val scheme = uri.scheme.orEmpty()
            val host = uri.host.orEmpty()
            val videoId = masterPlaylistUrl.getVideoID()
            String.format(M3U8_SEGMENT_URL_FORMAT, scheme, host, videoId, segment.url)
        }
    }
}

class M3U8ModelLoaderFactory : ModelLoaderFactory<String, Bitmap> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, Bitmap> {
        return M3U8ModelLoader()
    }

    override fun teardown() {}
}

class M3U8LoaderException : Exception()

private fun String.getVideoID(): String {
    return Uri.parse(this).getQueryParameter(M3U8_VIDEO_ID_QUERY_PARAM).orEmpty()
}
