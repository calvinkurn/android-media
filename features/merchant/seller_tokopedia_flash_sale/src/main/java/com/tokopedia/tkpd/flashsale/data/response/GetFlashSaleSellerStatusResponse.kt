package com.tokopedia.tkpd.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetFlashSaleSellerStatusResponse(
    @SerializedName("getFlashSaleSellerStatus")
    val getFlashSaleSellerStatus: GetFlashSaleSellerStatus = GetFlashSaleSellerStatus()
) {
    data class GetFlashSaleSellerStatus(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("admin_access")
        val adminAccess: AdminAccess = AdminAccess()
    ) {
        data class AdminAccess(
            @SerializedName("manage_flash_sale")
            val manageFlashSale: EligibilityInfo = EligibilityInfo()
        ) {
            data class EligibilityInfo(
                @SerializedName("is_device_allowed")
                val isDeviceAllowed: Boolean = true,
                @SerializedName("is_user_allowed")
                val isUserAllowed: Boolean = true
            )
        }

        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: Int = 0,
            @SerializedName("error_message")
            val errorMessage: List<String> = listOf(),
            @SerializedName("process_time")
            val processTime: Int = 0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}