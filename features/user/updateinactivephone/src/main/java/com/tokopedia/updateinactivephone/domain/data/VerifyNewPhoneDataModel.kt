package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.SerializedName

data class VerifyNewPhoneDataModel(
    @SerializedName("verifyNewPhoneInactivePhoneUser")
    var verify: VerifyDataModel = VerifyDataModel()
) {
    data class VerifyDataModel(
        @SerializedName("ErrorMessage")
        var errorMessage: String = "",
        @SerializedName("isSuccess")
        var isSuccess: Boolean = false)
}