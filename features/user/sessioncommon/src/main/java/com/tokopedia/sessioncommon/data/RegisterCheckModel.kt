package com.tokopedia.sessioncommon.data

import com.google.gson.annotations.SerializedName

data class RegisterCheckModel(
    @SerializedName("registerCheck")
    val data: RegisterCheckData = RegisterCheckData()
)

data class RegisterCheckData(
    @SerializedName("isExist")
    var isExist: Boolean = false,

    @SerializedName("errors")
    val errors: List<String> = emptyList(),

    @SerializedName("view")
    val view: String = ""
)
