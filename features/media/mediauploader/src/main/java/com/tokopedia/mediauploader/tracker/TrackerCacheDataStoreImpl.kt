package com.tokopedia.mediauploader.tracker

import com.tokopedia.mediauploader.common.VideoMetaDataExtractor
import com.tokopedia.mediauploader.common.data.entity.UploaderTracker
import com.tokopedia.mediauploader.common.data.store.base.CacheDataStore
import java.io.File
import javax.inject.Inject

class TrackerCacheDataStoreImpl @Inject constructor(
    private val metaDataExtractor: VideoMetaDataExtractor,
    store: CacheDataStore<UploaderTracker>
) : CacheDataStore<UploaderTracker> by store, TrackerCacheDataStore {

    override fun key(sourceId: String, originalPath: String): String {
        return "$sourceId$originalPath"
    }

    override suspend fun getData(key: String): UploaderTracker? {
        return get(key)
    }

    override suspend fun setCompressionTracker(
        key: String,
        data: UploaderTracker.() -> Unit
    ) = set(key, data)

    override suspend fun setOriginalVideoInfo(key: String, file: File) {
        set(key) {
            originalVideoPath = file.path
            videoOriginalSize = file.length().toString()
            originalVideoMetadata = metaDataExtractor.extract(file.path)
        }
    }

    override suspend fun setStartUploadTime(key: String, time: Long) {
        set(key) {
            startUploadTime = time
        }
    }

    override suspend fun setEndUploadTime(key: String, time: Long) {
        set(key) {
            endUploadTime = time
        }
    }
}
