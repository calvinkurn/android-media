package com.tokopedia.profilecompletion.changebiousername.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class UsernameValidationData(
    @SerializedName("data")
    val data: UsernameValidationResponse = UsernameValidationResponse()
)

data class UsernameValidationResponse(
    @SerializedName("feedXProfileValidateUsername")
    val response: UsernameValidation = UsernameValidation()
)

data class UsernameValidation(
    @SuppressLint("Invalid Data Type")
    @SerializedName("isValid")
    val isValid: Boolean = false,

    @SerializedName("notValidInformation")
    val errorMessage: String = ""
)