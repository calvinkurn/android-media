package com.tokopedia.mediauploader.common.data.store.datastore

import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import java.io.File

interface AnalyticsCacheDataStore {
    fun key(sourceId: String, originalPath: String): String

    suspend fun setCompressionInfo(key: String, data: UploaderTracker.() -> Unit)
    suspend fun setOriginalVideoInfo(key: String, file: File)
    suspend fun setStartUploadTime(key: String, time: Long)
    suspend fun setEndUploadTime(key: String, time: Long)

    suspend fun getData(key: String): UploaderTracker?
}
