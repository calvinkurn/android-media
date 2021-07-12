package com.tokopedia.managepassword.changepassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangePasswordResponseModel(
        @Expose
        @SerializedName("SubmitResetPassword")
        val changePassword: ChangePasswordData = ChangePasswordData()
)