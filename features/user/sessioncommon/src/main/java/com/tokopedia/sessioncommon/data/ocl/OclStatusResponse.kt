package com.tokopedia.sessioncommon.data.ocl

import com.google.gson.annotations.SerializedName

data class OclStatusResponse(
    @SerializedName("checker_ocl")
    val data: OclStatus
)

data class OclStatus(
    @SerializedName("is_showing")
    val isShowing: Boolean = false,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: String = ""
)
