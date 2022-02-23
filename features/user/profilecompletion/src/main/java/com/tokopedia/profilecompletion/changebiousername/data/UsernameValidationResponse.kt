package com.tokopedia.profilecompletion.changebiousername.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UsernameValidationData(
    @SerializedName("data")
    @Expose
    val data: UsernameValidationResponse = UsernameValidationResponse()
)
data class UsernameValidationResponse(
    @SerializedName("feedXProfileValidateUsername")
    @Expose
    val response: UsernameValidation = UsernameValidation()
)

data class UsernameValidation(
    @SerializedName("isValid")
    @Expose
    val isValid: Boolean = false,

    @SerializedName("notValidInformation")
    @Expose
    val errorMessage: String = ""
)