package com.tokopedia.mediauploader.data

import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface UploaderServices {

    @POST
    @Multipart
    suspend fun uploadFile(
            /*
            * get url from data policy
            * */
            @Url urlToUpload: String,

            /*
            * file_upload:
            * media blob (a file) to upload
            * */
            @Part fileUpload: MultipartBody.Part,

            /*
            * change time out at runtime
            * */
            @Header(HEADER_TIMEOUT) timeOut: String
    ) : MediaUploader

}