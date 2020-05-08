package com.tokopedia.shop.home.data.model

import com.google.gson.annotations.SerializedName

data class ShopLayoutWidget(
        @SerializedName("layoutID")
        val layoutId: String = "",
        @SerializedName("masterLayoutID")
        val masterLayoutID: Int = -1,
        @SerializedName("merchantTierID")
        val merchantTierID: Int = -1,
        @SerializedName("status")
        val status: Int = -1,
        @SerializedName("maxWidgets")
        val maxWidgets: Int = -1,
        @SerializedName("publishDate")
        val publishDate: String = "",
        @SerializedName("widgets")
        val listWidget: List<Widget> = listOf()

) {
    data class Response(
            @SerializedName("shopPageGetLayout")
            val shopLayoutWidget: ShopLayoutWidget = ShopLayoutWidget()
    )

    data class Widget(
            @SerializedName("widgetID")
            val widgetID: String = "",
            @SerializedName("widgetMasterID")
            val widgetMasterID: String = "",
            @SerializedName("layoutOrder")
            val layoutOrder: Int = -1,
            @SerializedName("name")
            val name: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("header")
            val header: Header = Header(),
            @SerializedName("data")
            val data: List<Data> = listOf()
    ) {
        data class Header(
                @SerializedName("title")
                val title: String = "",
                @SerializedName("ctaText")
                val ctaText: String = "",
                @SerializedName("ctaLink")
                val ctaLink: String = "",
                @SerializedName("cover")
                val cover: String = "",
                @SerializedName("ratio")
                val ratio: String = "",
                @SerializedName("isATC")
                val isAtc: Int = 0
        )

        data class Data(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("appLink")
                val appLink: String = "",
                @SerializedName("webLink")
                val webLink: String = "",
                @SerializedName("videoUrl")
                val videoUrl: String = "",
                @SerializedName("linkUrl")
                val linkUrl: String = "",
                @SerializedName("productID")
                val productID: Int = -1,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("displayPrice")
                val displayPrice: String = "",
                @SerializedName("originalPrice")
                val originalPrice: String = "",
                @SerializedName("discountPercentage")
                val discountPercentage: String = "",
                @SerializedName("voucherID")
                val voucherID: Int = -1,
                @SerializedName("voucherType")
                val voucherType: VoucherType,
                @SerializedName("voucherCode")
                val voucherCode: String = "",
                @SerializedName("amount")
                val amount: Amount = Amount(),
                @SerializedName("minimumSpend")
                val minimumSpend: Int = -1,
                @SerializedName("minimumSpendFormatted")
                val minimumSpendFormatted: String = "",
                @SerializedName("owner")
                val owner: Owner = Owner(),
                @SerializedName("validThru")
                val validThru: String = "",
                @SerializedName("tnc")
                val tnc: String = "",
                @SerializedName("inUseExpiry")
                val inUseExpiry: String = "",
                @SerializedName("productUrl")
                val productUrl: String = "",
                @SerializedName("isShowFreeOngkir")
                val isShowFreeOngkir: Boolean = false,
                @SerializedName("freeOngkirPromoIcon")
                val freeOngkirPromoIcon: String = "",
                @SerializedName("isSoldOut")
                val isSoldOut: Boolean = false,
                @SerializedName("rating")
                val rating: Double = 0.0,
                @SerializedName("totalReview")
                val totalReview: String = "",
                @SerializedName("isPO")
                val isPO: Boolean = false,
                @SerializedName("cashback")
                val cashback: Int = -1,
                @SerializedName("status")
                val status: Status = Status()
        ) {
            data class VoucherType(
                    @SerializedName("voucherType")
                    val voucherType: Int = -1,
                    @SerializedName("identifier")
                    val identifier: String = ""
            )

            data class Amount(
                    @SerializedName("amountType")
                    val amountType: Int = -1,
                    @SerializedName("amount")
                    val amount: Int = -1,
                    @SerializedName("amountFormatted")
                    val amountFormatted: String = ""
            )

            data class Owner(
                    @SerializedName("ownerId")
                    val ownerId: Int = -1,
                    @SerializedName("identifier")
                    val identifier: String = ""
            )

            data class Status(
                    @SerializedName("status")
                    val status: Int = -1,
                    @SerializedName("identifier")
                    val identifier: String = ""
            )
        }
    }
}