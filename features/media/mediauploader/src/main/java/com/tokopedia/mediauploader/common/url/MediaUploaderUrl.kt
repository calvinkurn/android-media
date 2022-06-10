package com.tokopedia.mediauploader.common.url

import com.tokopedia.url.Env
import javax.inject.Inject

class MediaUploaderUrl @Inject constructor(
    private val envManager: EnvManager
) {

    /**
     * A few sourceId still got `upedia.tokopedia.net` as host for video uploader,
     * so we need to hardcoded the base vod url with current env state.
     */
    private val baseVodUrl: String
        get() = if (envManager.getEnv() == Env.LIVE) {
            VOD_URL_LIVE
        } else {
            VOD_URL_STAGING
        }

    fun imageUpload(baseUrl: String, sourceId: String) = "$baseUrl/v1/upload/image/$sourceId"

    fun simpleVodUpload(sourceId: String) = "$baseVodUrl/video/upload/simple/$sourceId"

    fun largeInit() = "$baseVodUrl/video/upload/init"

    fun largePart() = "$baseVodUrl/video/upload/part"

    fun largeComplete() = "$baseVodUrl/video/upload/complete"

    fun hasLargeTranscodeStatus(uploadId: String) = "$baseVodUrl/video/transcode/status/$uploadId"

    fun largeAbortUpload() = "$baseVodUrl/video/upload/abort"

    companion object {
        private const val VOD_URL_LIVE = "https://vod.tokopedia.com/v2"
        private const val VOD_URL_STAGING = "https://vod-staging.tokopedia.com/v2"
    }

}