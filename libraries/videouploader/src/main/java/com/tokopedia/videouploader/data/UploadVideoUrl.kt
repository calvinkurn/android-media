package com.tokopedia.videouploader.data

/**
 * @author by nisie on 13/03/19.
 */
class UploadVideoUrl{

    companion object{
        const val BASE_URL = "http://10.255.15.68"

        const val URL_GENERATE_TOKEN = "$BASE_URL/v1/video/token"
        const val URL_UPLOAD_VIDEO = "$BASE_URL/v1/video/upload"
        const val URL_GET_VIDEO_INFO= "$BASE_URL/video/info"
    }


}