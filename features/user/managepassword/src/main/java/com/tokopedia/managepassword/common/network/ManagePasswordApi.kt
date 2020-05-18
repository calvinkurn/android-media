package com.tokopedia.managepassword.common.network

import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordDataModel
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordDataModel
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ManagePasswordApi {

    @FormUrlEncoded
    @Headers("x-tkpd-path: /${ManagePasswordApiUrl.ADD_PASSWORD}")
    @POST(ManagePasswordApiUrl.ADD_PASSWORD)
    suspend fun createPassword(
            @FieldMap params: HashMap<String, Any>
    ): AddPasswordDataModel

    @FormUrlEncoded
    @Headers("x-tkpd-path: /${ManagePasswordApiUrl.FORGOT_PASSWORD}")
    @POST(ManagePasswordApiUrl.FORGOT_PASSWORD)
    suspend fun resetPassword(
            @FieldMap params: HashMap<String, Any>
    ): ForgotPasswordDataModel
}