package com.tokopedia.mediauploader.data.consts

object UrlBuilder {

    private const val API_VERSION = "v1"
    private const val UPLOAD_PATH = "upload"
    private const val MEDIA_TYPE = "image"

    fun generate(baseUrl: String, sourceId: String): String {
        return "$baseUrl/$API_VERSION/$UPLOAD_PATH/$MEDIA_TYPE/$sourceId"
    }

}