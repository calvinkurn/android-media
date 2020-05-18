package com.tokopedia.managepassword.common.network

import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordDataModel
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordDataModel
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ManagePasswordApi {

    @FormUrlEncoded
    @POST(ManagePasswordApiUrl.ADD_PASSWORD)
    suspend fun createPassword(
            @FieldMap params: HashMap<String, Any>
    ): AddPasswordDataModel

    @FormUrlEncoded
    @POST(ManagePasswordApiUrl.FORGOT_PASSWORD)
    suspend fun resetPassword(
            @FieldMap params: HashMap<String, Any>
    ): ForgotPasswordDataModel
}