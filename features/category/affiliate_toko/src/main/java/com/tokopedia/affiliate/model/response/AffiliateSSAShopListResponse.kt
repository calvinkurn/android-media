package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateSSAShopListResponse(
    @SerializedName("data")
    val data: Data? = null
) {
    data class Data(
        @SerializedName("Status")
        val status: Int? = null,

        @SerializedName("PageInfo")
        val pageInfo: PageInfo? = null,

        @SerializedName("Error")
        val error: Error? = null,

        @SerializedName("ShopData")
        val shopData: List<ShopDataItem?>? = null
    ) {

        data class PageInfo(
            @SerializedName("hasNext")
            val hasNext: Boolean? = null,
            @SerializedName("hasPrev")
            val hasPrev: Boolean? = null,
            @SerializedName("currentPage")
            val currentPage: Int? = null,
            @SerializedName("totalPage")
            val totalPage: Int? = null,
            @SerializedName("totalCount")
            val totalCount: Int? = null
        )

        data class Error(
            @SerializedName("Message")
            val message: String? = null,

            @SerializedName("ErrorCode")
            val errorCode: String? = null,

            @SerializedName("Reason")
            val reason: String? = null
        )

        data class ShopDataItem(
            @SerializedName("SSAShopDetail")
            val ssaShopDetail: SSAShopDetail? = null,

            @SerializedName("SSACommissionDetail")
            val ssaCommissionDetail: SSACommissionDetail? = null
        ) {

            data class SSACommissionDetail(

                @SerializedName("CumulativePercentageFormatted")
                val cumulativePercentageFormatted: String? = null,

                @SerializedName("CumulativePercentage")
                val cumulativePercentage: Double? = null,

                @SerializedName("ExpiredDate_formatted")
                val expiredDateFormatted: String? = null,

                @SerializedName("SellerPercentageformatted")
                val sellerPercentageFormatted: String? = null,

                @SerializedName("SellerPercentage")
                val sellerPercentage: Double? = null,

                @SerializedName("ExpiredDate")
                val expiredDate: String? = null
            )

            data class SSAShopDetail(

                @SerializedName("QuantitySold")
                val quantitySold: Int? = null,

                @SerializedName("URLDetail")
                val uRLDetail: URLDetail? = null,

                @SerializedName("ShopName")
                val shopName: String? = null,

                @SerializedName("Message")
                val message: String? = null,

                @SerializedName("ShopType")
                val shopType: String? = null,

                @SerializedName("BadgeURL")
                val badgeURL: String? = null,

                @SerializedName("Rating")
                val rating: Double? = null,

                @SerializedName("ShopLocation")
                val shopLocation: String? = null,

                @SerializedName("SSAMessage")
                val ssaMessage: String? = null,

                @SerializedName("Label")
                val label: Label? = null,

                @SerializedName("ImageURL")
                val imageURL: ImageURL? = null,

                @SerializedName("SSAStatus")
                val ssaStatus: Boolean? = null,

                @SerializedName("ShopId")
                val shopId: Long? = null
            ) {

                data class ImageURL(

                    @SerializedName("IosURL")
                    val iosURL: String? = null,

                    @SerializedName("DesktopURL")
                    val desktopURL: String? = null,

                    @SerializedName("MobileURL")
                    val mobileURL: String? = null,

                    @SerializedName("AndroidURL")
                    val androidURL: String? = null
                )

                data class URLDetail(

                    @SerializedName("IosURL")
                    val iosURL: String? = null,

                    @SerializedName("DesktopURL")
                    val desktopURL: String? = null,

                    @SerializedName("MobileURL")
                    val mobileURL: String? = null,

                    @SerializedName("AndroidURL")
                    val androidURL: String? = null
                )

                data class Label(
                    @SerializedName("LabelType")
                    val labelType: String? = null,

                    @SerializedName("LabelText")
                    val labelText: String? = null
                )
            }
        }
    }
}
