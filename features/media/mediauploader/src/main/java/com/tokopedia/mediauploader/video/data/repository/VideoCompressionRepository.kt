package com.tokopedia.mediauploader.video.data.repository

import com.tokopedia.mediauploader.common.internal.VideoCompressionCacheManager
import com.tokopedia.mediauploader.video.data.params.VideoCompressionParam
import javax.inject.Inject

interface VideoCompressionRepository {
    suspend fun compress(param: VideoCompressionParam): String
}

internal class VideoCompressionRepositoryImpl @Inject constructor(
    private val cacheManager: VideoCompressionCacheManager
) : VideoCompressionRepository {

    override suspend fun compress(param: VideoCompressionParam): String {
        val (sourceId, path, bitrate, resolution) = param

        val cache = cacheManager.get(sourceId, path)

        if (cache != null) {
            return cache.compressedVideoPath
        }



        return ""
    }
}
