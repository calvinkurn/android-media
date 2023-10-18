package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class GetInitiateVoucherPageResponse(
    @SerializedName("getInitiateVoucherPage")
    val getInitiateVoucherPage: GetInitiateVoucherPage = GetInitiateVoucherPage()
) {
    data class GetInitiateVoucherPage(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
    ) {
        data class Data(
            @SerializedName("access_token")
            val accessToken: String = "",
            @SerializedName("is_eligible")
            val isEligible: Int = 0,
            @SerializedName("max_product")
            val maxProduct: Int = 0,
            @SerializedName("prefix_voucher_code")
            val prefixVoucherCode: String = "",
            @SerializedName("shop_id")
            val shopId: String = "",
            @SerializedName("token")
            val token: String = "",
            @SerializedName("user_id")
            val userId: String = "",
            @SerializedName("discount_active")
            val discountActive: Boolean = false
        )

        data class Header(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("messages")
            val messages: List<String> = listOf(),
            @SerializedName("reason")
            val reason: String = ""
        )
    }
}
