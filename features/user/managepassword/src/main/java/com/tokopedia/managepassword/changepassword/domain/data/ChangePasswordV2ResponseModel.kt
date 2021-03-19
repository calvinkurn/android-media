package com.tokopedia.managepassword.changepassword.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChangePasswordV2ResponseModel(
        @Expose
        @SerializedName("submitResetPasswordV2")
        val changePassword: ChangePasswordData = ChangePasswordData()
)