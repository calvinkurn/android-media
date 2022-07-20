package com.tokopedia.updateinactivephone.domain.data

import com.google.gson.annotations.SerializedName

data class RegisterCheckModel(
    @SerializedName("registerCheck")
    val data: RegisterCheckData = RegisterCheckData()
)

data class RegisterCheckData(
    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("errors")
    val errors: List<String> = emptyList()
)