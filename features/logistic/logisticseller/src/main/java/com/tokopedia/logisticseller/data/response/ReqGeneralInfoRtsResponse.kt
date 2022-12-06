package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class ReqGeneralInfoRtsResponse(
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("message_error")
    val messageError: String = "",
) {
    val isSuccess: Boolean
        get() = status == STATUS_SUCCESS

    companion object {
        private const val STATUS_SUCCESS = 200
    }
}
