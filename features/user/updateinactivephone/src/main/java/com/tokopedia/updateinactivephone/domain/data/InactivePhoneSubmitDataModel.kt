package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InactivePhoneSubmitDataModel(
        @Expose @SerializedName("submitInactivePhoneUser")
        val status: SubmitInactivePhoneUser = SubmitInactivePhoneUser()
) {
    data class SubmitInactivePhoneUser(
            @Expose @SerializedName("isSuccess")
            val isSuccess: Boolean = false,
            @Expose @SerializedName("errorMessage")
            val errorMessage: String = ""
    )
}