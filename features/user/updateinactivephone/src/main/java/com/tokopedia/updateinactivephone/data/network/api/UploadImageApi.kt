package com.tokopedia.updateinactivephone.data.network.api

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.model.request.UploadHostResponse
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import okhttp3.RequestBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface UploadImageApi {

    @Multipart
    @POST("")
    suspend fun uploadImage(@Url url: String,
                    @QueryMap params: Map<String, String>,
                    @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody): UploadImageData

    @FormUrlEncoded
    @POST(UpdateInactivePhoneURL.GET_UPLOAD_HOST)
    suspend fun getUploadHost(@FieldMap params: Map<String, @JvmSuppressWildcards Any>): UploadHostResponse
}
