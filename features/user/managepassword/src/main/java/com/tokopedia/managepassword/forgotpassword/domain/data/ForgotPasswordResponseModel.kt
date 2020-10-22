package com.tokopedia.managepassword.forgotpassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponseModel(
        @Expose
        @SerializedName("resetPassword")
        val resetPassword: ForgotPasswordModel = ForgotPasswordModel()
) {
    data class ForgotPasswordModel(
            @Expose
            @SerializedName("redirect_url")
            val redirectUrl: String = "",
            @Expose
            @SerializedName("is_success")
            val isSuccess: Boolean = false,
            @Expose
            @SerializedName("message")
            val message: String = ""
    )
}