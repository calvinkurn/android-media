package com.tokopedia.mediauploader.video.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.common.internal.VideoCompressionCacheManager
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.min

interface VideoCompressionRepository {
    suspend fun compress(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String
}

internal class VideoCompressionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cacheManager: VideoCompressionCacheManager
) : VideoCompressionRepository {

    override suspend fun compress(
        progressUploader: ProgressUploader?,
        param: VideoCompressionParam
    ): String {
        val (sourceId, path, bitrate, resolution) = param

        val cache = cacheManager.get(sourceId, path)
        if (cache != null) return cache.compressedVideoPath

        val videoOriginalInfo = getOriginalVideoInfo(path)?: VideoInfo()
        val minVideoRes = min(videoOriginalInfo.width, videoOriginalInfo.height)

        if (minVideoRes > resolution || videoOriginalInfo.bitrate > bitrate) {
            // compress

            return ""
        }

        return path
    }

    private fun getOriginalVideoInfo(path: String): VideoInfo? {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, path.toUri())
        } catch (exception: IllegalArgumentException) {
            Timber.d("VID-Compression: $exception")
            return null
        }

        return VideoInfo(
            width = getVideoWidth(retriever),
            height = getVideoHeight(retriever),
            bitrate = getVideoBitrate(retriever)
        )
    }

    private fun getVideoBitrate(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_BITRATE
        )?.toInt() ?: 0
    }

    private fun getVideoWidth(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
        )?.toInt() ?: 0
    }

    private fun getVideoHeight(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
        )?.toInt() ?: 0
    }

    data class VideoInfo(
        val width: Int = 0,
        val height: Int = 0,
        val bitrate: Int = 0
    )
}
