package com.tokopedia.mediauploader.image.data

import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import com.tokopedia.mediauploader.common.util.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ImageUploadServices {

    @POST
    @Multipart
    suspend fun uploadImage(
        /*
        * get url from data policy
        * */
        @Url urlToUpload: String,

        /*
        * file_upload:
        * an multiple-form data to retrieve the data
        * */
        @Part partBody: MultipartBody.Part,

        /*
        * change time out at runtime
        * */
        @Header(HEADER_TIMEOUT) timeOut: String
    ) : ImageUploader

}