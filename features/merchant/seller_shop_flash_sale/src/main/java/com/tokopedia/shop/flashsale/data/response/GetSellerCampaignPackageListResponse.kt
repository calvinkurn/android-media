package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class GetSellerCampaignPackageListResponse(
    @SerializedName("getSellerCampaignPackageList")
    val getSellerCampaignPackageList: GetSellerCampaignPackageList = GetSellerCampaignPackageList()
) {
    data class GetSellerCampaignPackageList(
        @SerializedName("package_list")
        val packageList: List<Package> = listOf(),
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader()
    ) {
        data class Package(
            @SerializedName("remaining_quota")
            val remainingQuota: Int = 0,
            @SerializedName("current_quota")
            val currentQuota: Int = 0,
            @SerializedName("is_disabled")
            val isDisabled: Boolean = false,
            @SerializedName("original_quota")
            val originalQuota: Int = 0,
            @SerializedName("package_end_time")
            val packageEndTime: String = "",
            @SerializedName("package_id")
            val packageId: String = "",
            @SerializedName("package_name")
            val packageName: String = "",
            @SerializedName("package_start_time")
            val packageStartTime: String = ""
        )

        data class ResponseHeader(
            @SerializedName("processTime")
            val processTime: Double = 0.0,
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )
    }
}