package com.tokopedia.updateinactivephone.data.network.api

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.model.request.UploadHostResponse
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import okhttp3.RequestBody
import retrofit2.http.*

interface UploadImageApi {

    @Multipart
    @POST("")
    suspend fun uploadImage(@Url url: String,
                            @Part("userId") userId: RequestBody,
                            @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody): UploadImageData

    @FormUrlEncoded
    @POST(UpdateInactivePhoneURL.GET_UPLOAD_HOST)
    suspend fun getUploadHost(@FieldMap params: Map<String, @JvmSuppressWildcards Any>): UploadHostResponse
}
