package com.tokopedia.loginregister.login_sdk.data

import com.google.gson.annotations.SerializedName

data class ValidateClientResponse (
    @SerializedName("validate_client_signature")
    val data: ValidateClientData
)

data class ValidateClientData(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("error")
    var error: String = ""
)
