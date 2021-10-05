package com.tokopedia.mediauploader.data

import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import okhttp3.MultipartBody
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
    ) : MediaUploader


}