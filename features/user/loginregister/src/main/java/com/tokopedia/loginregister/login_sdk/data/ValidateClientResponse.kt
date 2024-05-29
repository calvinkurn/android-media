package com.tokopedia.loginregister.login_sdk.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ValidateClientResponse (
    @SerializedName("validate_client_signature")
    val data: ValidateClientData
)

data class ValidateClientData(
    @SuppressLint("Invalid Data Type")
    @SerializedName("is_valid")
    var status: Boolean = false,
    @SerializedName("error")
    var error: String = "",
    @SerializedName("app_name")
    var appName: String = ""
)
