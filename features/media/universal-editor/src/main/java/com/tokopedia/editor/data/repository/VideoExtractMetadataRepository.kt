package com.tokopedia.editor.data.repository

import android.content.Context
import android.media.MediaMetadataRetriever
import androidx.core.net.toUri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.editor.analytics.EditorLogger
import com.tokopedia.editor.data.model.VideoInfo
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import javax.inject.Inject

interface VideoExtractMetadataRepository {
    fun extract(path: String): VideoInfo
}

class VideoExtractMetadataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : VideoExtractMetadataRepository {

    override fun extract(path: String): VideoInfo {
        val retriever = MediaMetadataRetriever()

        try {
            retriever.setDataSource(context, path.toUri())
        } catch (exception: IllegalArgumentException) {
            EditorLogger.videoExtractMetadata(
                exception.stackTraceToString().take(1000)
            )
        }

        return VideoInfo(
            width = getVideoWidth(retriever),
            height = getVideoHeight(retriever)
        )
    }

    private fun getVideoWidth(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
        )?.toIntOrZero() ?: MIN_WIDTH
    }

    private fun getVideoHeight(retriever: MediaMetadataRetriever): Int {
        return retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
        )?.toIntOrZero() ?: MIN_HEIGHT
    }

    companion object {
        private const val MIN_WIDTH = 480
        private const val MIN_HEIGHT = 853
    }
}
