package com.tokopedia.mediauploader.common.internal

import com.tokopedia.mediauploader.common.util.MediaUploaderNetwork.BASE_URL
import javax.inject.Inject

class MediaUploaderUrl @Inject constructor(
    private val policyManager: SourcePolicyManager
) {

    private val host: String
        get() = policyManager.get()?.host ?: BASE_URL

    fun imageUpload(sourceId: String)
        = "$host/$UPEDIA_VERSION/upload/image/$sourceId"

    fun imageUploadSecure(sourceId: String)
        = "http://uploadpedia-secure.service.aws-main-staging-ap-southeast-1-staging.consul/v1/upload/image/geDGcq"

//    fun imageUploadSecure(sourceId: String) = "https://chat.tokopedia.com/tc/v1/upload_secure"

    fun simpleVodUpload(sourceId: String)
        = "$host/$VOD_VERSION/video/upload/simple/$sourceId"

    fun largeInit()
        = "$host/$VOD_VERSION/video/upload/init"

    fun largePart()
        = "$host/$VOD_VERSION/video/upload/part"

    fun largeComplete()
        = "$host/$VOD_VERSION/video/upload/complete"

    fun hasLargeTranscodeStatus(uploadId: String)
        = "$host/$VOD_VERSION/video/transcode/status/$uploadId"

    fun largeAbortUpload()
        = "$host/$VOD_VERSION/video/upload/abort"

    companion object {
        private const val UPEDIA_VERSION = "v1"
        private const val VOD_VERSION = "v2"
    }

}
