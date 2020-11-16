package com.tokopedia.updateinactivephone.revamp.domain.api

import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadParamDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import okhttp3.RequestBody
import retrofit2.http.*

interface InactivePhoneApi {

    @Multipart
    @POST("")
    suspend fun uploadImage(
            @Url url: String,
            @Body params: ImageUploadParamDataModel,
            @Part("fileToUpload\"; filename=\"image.jpg") imageFile: RequestBody
    ): ImageUploadDataModel

    @FormUrlEncoded
    @POST(GET_UPLOAD_HOST)
    suspend fun getUploadHost(
            @FieldMap params: Map<String, @JvmSuppressWildcards Any>
    ): UploadHostDataModel

    companion object {
        private const val GET_UPLOAD_HOST = "apps/notactive/image/upload-host"
    }
}