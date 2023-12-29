package com.tokopedia.mediauploader.video.internal

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import timber.log.Timber
import javax.inject.Inject

interface VideoMetaDataExtractor {
    fun extract(path: String): VideoInfo?
}

class VideoMetaDataExtractorImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VideoMetaDataExtractor {

    /**
     * This method will extract out the video info based on file path. The method use the MetadataRetriever's API
     * to gathering the video file. The method will cater the width, height, and bitrate of the video.
     *
     * The return value is nullable which somehow the metadataRetriever failed to extract the info.
     *
     * @param path
     * @return [VideoInfo]
     */
    override fun extract(path: String): VideoInfo? {
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
            bitrate = getVideoBitrate(retriever),
            duration = getVideoDuration(retriever)
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
        )?.toInt() ?: MIN_WIDTH
    }

    private fun getVideoHeight(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
        )?.toInt() ?: MIN_HEIGHT
    }

    private fun getVideoDuration(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toInt() ?: 0
    }

    companion object {
        private const val MIN_WIDTH = 368
        private const val MIN_HEIGHT = 640
    }
}
