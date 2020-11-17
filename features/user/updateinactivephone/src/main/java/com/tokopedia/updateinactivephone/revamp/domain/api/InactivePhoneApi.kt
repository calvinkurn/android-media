package com.tokopedia.updateinactivephone.revamp.domain.api

import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.ImageUploadParamDataModel
import com.tokopedia.updateinactivephone.revamp.domain.data.UploadHostDataModel
import okhttp3.RequestBody
import retrofit2.http.*

interface InactivePhoneApi {

    @Multipart
    @POST("userapp/api/v1/sq-inactive-phone/upload-photo")
    suspend fun uploadImage(
            @Part("index") userIndex: RequestBody,
            @Part("old_msisdn") oldMsisdn: RequestBody,
            @Part("email") email: RequestBody,
            @Part("file\"; filename=\"inactivePhone.jpg") file: RequestBody
    ): ImageUploadDataModel
}