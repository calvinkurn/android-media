package com.tokopedia.mediauploader.video.data.internal

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.mediauploader.video.data.params.LargeUploadCacheParam
import java.io.File

interface LargeUploadState {
    fun set(sourceId: String, state: LargeUploadCacheParam)
    fun get(sourceId: String, fileName: String): LargeUploadCacheParam?
    fun setPartNumber(sourceId: String, fileName: String, partNumber: Int)
    fun clear()
}

class LargeUploadStateHandler(
    context: Context
) : LocalCacheHandler(context, NAME_PREFERENCE_LARGE_UPLOAD), LargeUploadState {

    override fun set(sourceId: String, state: LargeUploadCacheParam) {
        val content = Gson().toJson(state)
        val cacheKey = key(sourceId, state.filePath)
        putString(cacheKey, content)

        applyEditor()
    }

    override fun get(sourceId: String, fileName: String): LargeUploadCacheParam? {
        val cacheKey = key(sourceId, fileName)
        val content = getString(cacheKey, "")

        if (content.isEmpty()) {
            return null
        }

        return Gson().fromJson(content, LargeUploadCacheParam::class.java)
    }

    override fun setPartNumber(sourceId: String, fileName: String, partNumber: Int) {
        get(sourceId, fileName)?.let {
            val updated = it.copy(partNumber = partNumber)
            set(sourceId, updated)
        }
    }

    override fun clear() {
        clearCache()
    }

    private fun key(sourceId: String, filePath: String): String {
        val filename = File(filePath).name
        return sourceId + filename
    }

    companion object {
        private const val NAME_PREFERENCE_LARGE_UPLOAD = "large_upload_state"
    }

}