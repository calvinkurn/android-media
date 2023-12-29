package com.tokopedia.mediauploader.image.data

import com.tokopedia.mediauploader.common.util.interceptor.NetworkTimeOutInterceptor.Companion.HEADER_TIMEOUT
import com.tokopedia.mediauploader.image.data.entity.ImageUploader
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface ImageUploadServices {

    @Headers("Accept: application/json")
    @Multipart
    @POST
    suspend fun uploadImage(
        @Url urlToUpload: String,
        @Part partBody: MultipartBody.Part,
        @PartMap partMap: HashMap<String, RequestBody>,
        @HeaderMap headerMap: HashMap<String, String>,
        @Header(HEADER_TIMEOUT) timeOut: String
    ): ImageUploader

}
