package com.tokopedia.mediauploader.common.internal

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import javax.inject.Inject

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
        val compressedTime: String,
        val videoOriginalSize: String,
        val videoCompressedSize: String
    )

    companion object {
        private const val NAME_PREFERENCE_VID_COMPRESS = "vid_compress_cache"
    }
}
