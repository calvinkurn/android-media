package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

data class UpdateQuotaResponse(
    @SerializedName("merchantPromotionUpdateMVQuota")
    val updateVoucher: UpdateVoucherDataModel = UpdateVoucherDataModel()
)

data class UpdateVoucherDataModel(
    @SerializedName("status")
    val responseStatus: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("process_time")
    val processTime: Float = 0f,
    @SerializedName("data")
    val updateVoucherSuccessData: UpdateVoucherSuccessData = UpdateVoucherSuccessData()
)

data class UpdateVoucherSuccessData(
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("voucher_id")
    val voucherId: String = "",
    @SerializedName("status")
    val status: String = ""
) {

    fun getIsSuccess() = status == UpdateResponse.STATUS_SUCCESS
}

object UpdateResponse {
    internal const val STATUS_SUCCESS = "Success"
}
