package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

data class CancelVoucherResponse (
    @SerializedName("merchantPromotionUpdateStatusMV")
    val cancelVoucher: UpdateStatusVoucherDataModel = UpdateStatusVoucherDataModel()
)

data class UpdateStatusVoucherDataModel(
    @SerializedName("status")
    val responseStatus: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val updateStatusVoucherData: UpdateStatusVoucherData = UpdateStatusVoucherData()
)

data class UpdateStatusVoucherData(
    @SerializedName("voucher_id")
    val voucherId: String = "",
    @SerializedName("status")
    val status: String = "") {

    fun getIsSuccess() = status == CancelUpdateResponse.STATUS_SUCCESS
}

object CancelUpdateResponse {
    internal const val STATUS_SUCCESS = "Success"
}
