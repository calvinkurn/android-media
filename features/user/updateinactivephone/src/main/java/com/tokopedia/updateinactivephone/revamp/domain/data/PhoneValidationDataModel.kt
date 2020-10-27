package com.tokopedia.updateinactivephone.revamp.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class PhoneValidationDataModel (
        @SerializedName("ValidateInactivePhoneUser")
        @Expose
        var validation: Validation = Validation()
) {
    data class Validation (
            @SerializedName("isSuccess")
            @Expose
            var isSuccess: Boolean = false,
            @SerializedName("status")
            @Expose
            var status: Int = 0,
            @SerializedName("errorMessage")
            @Expose
            var error: String = ""
    )
}