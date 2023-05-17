package com.tokopedia.mediauploader.common.cache

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import java.io.File
import javax.inject.Inject

typealias CacheCompressionModel = VideoCompressionCacheManager.Data

class VideoCompressionCacheManager @Inject constructor(
    @ApplicationContext context: Context
) : LocalCacheHandler(context, NAME_PREFERENCE_VID_COMPRESS) {

    fun set(sourceId: String, data: Data) {
        val content = Gson().toJson(data)
        val cacheKey = key(sourceId, data.originalVideoPath)

        putString(cacheKey, content)

        applyEditor()
    }

    fun get(sourceId: String, filePath: String): Data? {
        val cacheKey = key(sourceId, filePath)
        val data = getString(cacheKey, "")

        if (data.isEmpty()) {
            return null
        }

        return Gson().fromJson(data, Data::class.java)
    }

    private fun key(sourceId: String, filePath: String) = "$sourceId#$filePath"

    data class Data(
        val originalVideoPath: String,
        val compressedVideoPath: String,
        val compressedTime: Long,
        val videoOriginalSize: String,
        val videoCompressedSize: String,
        val originalVideoMetadata: VideoInfo?,
        val compressedVideoMetadata: VideoInfo?
    ) {

        fun isCompressedFileExist(): Boolean {
            return File(compressedVideoPath).exists()
        }
    }

    companion object {
        private const val NAME_PREFERENCE_VID_COMPRESS = "vid_compress_cache"
    }
}
