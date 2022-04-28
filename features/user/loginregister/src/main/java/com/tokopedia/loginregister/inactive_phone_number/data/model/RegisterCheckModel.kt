package com.tokopedia.loginregister.inactive_phone_number.data.model

import com.google.gson.annotations.SerializedName

data class RegisterCheckModel (
    @SerializedName("registerCheck")
    val data: RegisterCheckData = RegisterCheckData()
)

data class RegisterCheckData(
    @SerializedName("isExist")
    var isExist: Boolean = false,

    @SerializedName("errors")
    val errors: List<String> = emptyList()
)