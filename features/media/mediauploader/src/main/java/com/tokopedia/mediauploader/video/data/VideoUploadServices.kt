package com.tokopedia.mediauploader.video.data

import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.video.data.entity.VideoUploader
import okhttp3.RequestBody
import retrofit2.http.*

interface VideoUploadServices {

    @POST
    @Multipart
    suspend fun uploadSingleVideo(
        /*
        * get url from data policy
        * */
        @Url urlToUpload: String,

        //TODO comment
        @Body body: RequestBody,

        /*
        * change time out at runtime
        * */
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : VideoUploader


}