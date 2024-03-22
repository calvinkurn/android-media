package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName

data class ValidateClientResponse (
    @SerializedName("validate_client_signature")
    val data: ValidateClientData
)

data class ValidateClientData(
    @SerializedName("is_valid")
    val status: Boolean,
    @SerializedName("error")
    var error: String = "",
    @SerializedName("app_name")
    var appName: String = ""
)
