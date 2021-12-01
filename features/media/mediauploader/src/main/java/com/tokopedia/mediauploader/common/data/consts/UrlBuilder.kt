package com.tokopedia.mediauploader.common.data.consts

object UrlBuilder {

    private const val BASE_VOD_UPLOAD_URL = "https://vod.tokopedia.com/v2"

    fun imageUploadUrl(baseUrl: String, sourceId: String): String {
        return "$baseUrl/v1/upload/image/$sourceId"
    }

    fun simpleUrl(sourceId: String): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/simple/$sourceId"
    }

    fun initUrl(): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/init"
    }

    fun isChunkUrl(): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/part"
    }

    fun uploadUrl(): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/part"
    }

    fun completeUrl(): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/complete"
    }

    fun isTranscodeUrl(uploadId: String): String {
        return "$BASE_VOD_UPLOAD_URL/video/transcode/status/$uploadId"
    }

    fun abortUrl(): String {
        return "$BASE_VOD_UPLOAD_URL/video/upload/abort"
    }

}