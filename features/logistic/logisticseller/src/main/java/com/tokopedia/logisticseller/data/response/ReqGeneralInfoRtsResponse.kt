package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class ReqGeneralInfoRtsResponse(
    @SerializedName("mpLogisticInsertActionGeneralInformation")
    val data: ReqGeneralInfoRts = ReqGeneralInfoRts()
) {
    data class ReqGeneralInfoRts(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("message_error")
        val messageError: String = ""
    )

    val isSuccess: Boolean
        get() = data.status == STATUS_SUCCESS

    companion object {
        private const val STATUS_SUCCESS = 200
    }
}
