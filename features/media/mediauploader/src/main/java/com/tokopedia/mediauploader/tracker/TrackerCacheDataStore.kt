package com.tokopedia.mediauploader.tracker

import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import java.io.File

interface TrackerCacheDataStore {
    fun key(sourceId: String, originalPath: String): String

    suspend fun setCompressionTracker(key: String, data: UploaderTracker.() -> Unit)
    suspend fun setOriginalVideoInfo(key: String, file: File)
    suspend fun setStartUploadTime(key: String, time: Long)
    suspend fun setEndUploadTime(key: String, time: Long)

    suspend fun getData(key: String): UploaderTracker?
}
