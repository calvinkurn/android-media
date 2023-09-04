package com.tokopedia.mediauploader.analytics

import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mediauploader.common.cache.LargeUploadStateCacheManager
import com.tokopedia.mediauploader.common.data.store.datastore.AnalyticsCacheDataStore
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.track.TrackApp
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MediaUploaderAnalytics @Inject constructor(
    private val uploadStateManager: LargeUploadStateCacheManager,
    @UploaderQualifier store: AnalyticsCacheDataStore
) : AnalyticsCacheDataStore by store {

    suspend fun sendEvent(sourceId: String, file: File, isRetry: Boolean, isSimpleUpload: Boolean) {
        val cacheKey = key(sourceId, file.path)
        val data = getData(cacheKey) ?: return

        val isCompressed = data.compressedVideoPath.isNotEmpty()
        val isRetryStatus = uploadStateManager.isChunkExist(sourceId, file.name) && isRetry
        val uploadType = uploadTypeLabel(isSimpleUpload)
        val uploadTime = timeInSec(data.startUploadTime, data.endUploadTime)
        val compressionTime = timeInSec(data.startCompressedTime, data.endCompressedTime)
        val sizeBefore = File(data.originalVideoPath).length().formattedToMB()
        val sizeAfter = File(data.compressedVideoPath).length().formattedToMB()
        val bitrate = data.originalVideoMetadata?.bitrate.orZero() / 1024
        val duration = data.originalVideoMetadata?.duration.orZero().fromMillisToSec()

        val label = isCompressed.toString()
            .label(isRetryStatus)
            .label(uploadType)
            .label(uploadTime)
            .label(compressionTime)
            .label(sizeBefore)
            .label(sizeAfter)
            .label(bitrate)
            .label(duration)
            .label(sourceId)

        val events = mutableMapOf(
            KEY_EVENT_CATEGORY to CATEGORY,
            KEY_EVENT_ACTION to ACTION,
            KEY_EVENT to EVENT,
            KEY_EVENT_LABEL to label,
            KEY_TRACKER_ID to TRACKER_ID,
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
        return if (isSimpleUpload) SIMPLE else LARGE
    }

    private fun String.label(`object`: Any?): String {
        return plus(" - $`object`")
    }

    companion object {
        // keys
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_TRACKER_ID = "trackerId"
        private const val KEY_CURRENT_SITE = "currentSite"
        private const val KEY_BUSINESS_UNIT = "businessUnit"

        // values
        private const val BUSINESS_UNIT = "media"
        private const val EVENT = "viewCommunicationIris"
        private const val CURRENT_SITE = "tokopediamarketplace"
        private const val ACTION = "user completed video upload"
        private const val CATEGORY = "vod compression"
        private const val TRACKER_ID = "43118"
        private const val SIMPLE = "simple"
        private const val LARGE = "large"
    }
}
