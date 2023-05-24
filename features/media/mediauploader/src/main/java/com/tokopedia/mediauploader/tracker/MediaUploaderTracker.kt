package com.tokopedia.mediauploader.tracker

import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.common.cache.LargeUploadStateCacheManager
import com.tokopedia.track.TrackApp
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MediaUploaderTracker @Inject constructor(
    private val uploadStateManager: LargeUploadStateCacheManager,
    store: TrackerCacheDataStore
) : TrackerCacheDataStore by store {

    suspend fun sendTracker(
        sourceId: String,
        file: File,
        isRetry: Boolean,
        isSimpleUpload: Boolean
    ) {
        val cacheKey = key(sourceId, file.path)
        val data = getData(cacheKey) ?: return

        val isCompressed = data.compressedVideoPath.isNotEmpty()
        val isRetryStatus = uploadStateManager.isChunkExist(sourceId, file.name) && isRetry
        val uploadType = uploadTypeLabel(isSimpleUpload)
        val uploadTime = timeInSec(data.startUploadTime, data.endUploadTime)
        val compressionTime = timeInSec(data.startCompressedTime, data.endCompressedTime)
        val sizeBefore = File(data.originalVideoPath).length().formattedToMB()
        val sizeAfter = File(data.compressedVideoPath).length().formattedToMB()
        val bitrate = data.compressedVideoMetadata?.bitrate.orZero() / 1024
        val duration = data.originalVideoMetadata?.duration.orZero().fromMillisToSec()

        val events = mutableMapOf(
            KEY_EVENT to "viewCommunicationIris",
            KEY_EVENT_CATEGORY to "vod compression",
            KEY_EVENT_ACTION to "user completed video upload",
            KEY_EVENT_LABEL to "$isCompressed - $isRetryStatus - $uploadType - $compressionTime - $uploadTime - $sizeBefore - $sizeAfter - $bitrate - $duration",
            KEY_TRACKER_ID to "43117",
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(events.toMap())
    }

    private fun timeInSec(start: Long?, end: Long?): String {
        val time = end.orZero() - start.orZero()
        return time.fromMillisToSec()
    }

    private fun Number.fromMillisToSec(): String {
        return TimeUnit.MILLISECONDS.toSeconds(this.toLong())
            .toFloat()
            .toString()
    }

    private fun uploadTypeLabel(isSimpleUpload: Boolean): String {
        return if (isSimpleUpload) "simple" else "large"
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_BUSINESS_UNIT = "businessUnit"

        private const val BUSINESS_UNIT = "media"
        private const val CURRENT_SITE = "tokopediamarketplace"
    }
}
