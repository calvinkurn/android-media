package com.tokopedia.mediauploader.common.cache

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.video.data.params.LargeUploadCacheParam
import java.io.File
import javax.inject.Inject

class LargeUploadStateCacheManager @Inject constructor(
    @ApplicationContext context: Context
) : LocalCacheHandler(context, NAME_PREFERENCE_LARGE_UPLOAD) {

    fun set(sourceId: String, state: LargeUploadCacheParam) {
        val content = Gson().toJson(state)
        val cacheKey = key(sourceId, state.filePath)
        putString(cacheKey, content)

        applyEditor()
    }

    fun get(sourceId: String, fileName: String): LargeUploadCacheParam? {
        val cacheKey = key(sourceId, fileName)
        val content = getString(cacheKey, "")

        if (content.isEmpty()) {
            return null
        }

        return Gson().fromJson(content, LargeUploadCacheParam::class.java)
    }

    fun setPartNumber(sourceId: String, fileName: String, partDone: Map<Int, Boolean>) {
        get(sourceId, fileName)?.let {
            val updated = it.copy(partDone = partDone)
            set(sourceId, updated)
        }
    }

    fun isChunkExist(sourceId: String, fileName: String): Boolean {
        return get(sourceId, fileName)?.partDone?.isNotEmpty() == true
    }

    fun clear() {
        clearCache()
    }

    private fun key(sourceId: String, filePath: String): String {
        val filename = File(filePath).name
        return "$sourceId#$filename"
    }

    companion object {
        private const val NAME_PREFERENCE_LARGE_UPLOAD = "large_upload_state"
    }

}
