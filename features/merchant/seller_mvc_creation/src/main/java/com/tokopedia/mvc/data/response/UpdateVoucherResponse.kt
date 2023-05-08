package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

data class UpdateVoucherResponse(
    @SerializedName("merchantPromotionUpdateMV")
    val updateVoucherModel: UpdateVoucherModel = UpdateVoucherModel()
) {
    data class UpdateVoucherModel(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("process_time")
        val processTime: Long = 0,
        @SerializedName("data")
        val data: UpdateVoucherData = UpdateVoucherData()
    ) {
        data class UpdateVoucherData(
            @SerializedName("redirect_url")
            val redirectUrl: String = "",
            @SerializedName("voucher_id")
            val voucherId: Long = 0,
            @SerializedName("status")
            val status: String = ""
        )

        fun getIsSuccess() = status == STATUS_CODE_SUCCESS
        private val STATUS_CODE_SUCCESS = 200
    }
}
