package com.tokopedia.mediauploader.common.cache

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import javax.inject.Inject

class TrackerCacheManager @Inject constructor(
    @ApplicationContext context: Context
) : LocalCacheHandler(context, NAME_PREFERENCE_VID_COMPRESS) {

    fun set(sourceId: String, data: UploaderTracker) {
        val content = Gson().toJson(data)
        val cacheKey = key(sourceId, data.originalVideoPath)

        putString(cacheKey, content)

        applyEditor()
    }

    fun get(sourceId: String, filePath: String): UploaderTracker? {
        val cacheKey = key(sourceId, filePath)
        val data = getString(cacheKey, "")

        if (data.isEmpty()) {
            return null
        }

        return Gson().fromJson(data, UploaderTracker::class.java)
    }

    private fun key(sourceId: String, filePath: String) = "$sourceId#$filePath"

    companion object {
        private const val NAME_PREFERENCE_VID_COMPRESS = "vid_compress_cache"
    }
}
