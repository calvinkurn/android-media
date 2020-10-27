package com.tokopedia.updateinactivephone.revamp.domain.api

import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL
import com.tokopedia.updateinactivephone.data.model.request.UploadHostResponse
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import okhttp3.RequestBody
import retrofit2.http.*

interface InactivePhoneApi {

    @Multipart
    @POST("")
    suspend fun uploadImage(
            @Url url: String,
            @Part("userId") userId: RequestBody,
            @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody
    ): ImageUploadDataModel

    @FormUrlEncoded
    @POST(UpdateInactivePhoneURL.GET_UPLOAD_HOST)
    suspend fun getUploadHost(
            @FieldMap params: Map<String, @JvmSuppressWildcards Any>
    ): UploadHostDataModel
}