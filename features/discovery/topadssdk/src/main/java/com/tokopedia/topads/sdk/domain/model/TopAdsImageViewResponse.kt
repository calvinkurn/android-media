package com.tokopedia.topads.sdk.domain.model


import com.google.gson.annotations.SerializedName

data class TopAdsImageViewResponse(
    @SerializedName("data")
    var `data`: List<Data?>? = null,
    @SerializedName("header")
    val header: Header? = null,
    @SerializedName("status")
    val status: Status? = null
) {
    data class Data(
        @SerializedName("ad_click_url")
        val adClickUrl: String? = "",
        @SerializedName("ad_view_url")
        var adViewUrl: String? = "",
        @SerializedName("applinks")
        val applinks: String? = "",
        @SerializedName("banner")
        val banner: Banner? = null,
        @SerializedName("id")
        val id: String? = "",
        @SerializedName("redirect")
        val redirect: String? = ""
    ) {
        data class Banner(
            @SerializedName("badges")
            val badges: List<Badge?>? = null,
            @SerializedName("images")
            var images: List<Image?>? = null,
            @SerializedName("name")
            val name: String? = "",
            @SerializedName("shop")
            val shop: Shop? = null
        ) {
            data class Badge(
                @SerializedName("image_url")
                val imageUrl: String? = "",
                @SerializedName("show")
                val show: Boolean = false,
                @SerializedName("title")
                val title: String? = ""
            )

            data class Image(
                @SerializedName("full_ecs")
                val fullEcs: String? = "",
                @SerializedName("image_dimension")
                val imageDimension: ImageDimension? = null
            ) {
                data class ImageDimension(
                    @SerializedName("dimen_height")
                    val dimenHeight: Int?,
                    @SerializedName("dimen_id")
                    val dimenId: Int?,
                    @SerializedName("dimen_width")
                    val dimenWidth: Int?
                )
            }

            data class Shop(
                @SerializedName("city")
                val city: String? = "",
                @SerializedName("domain")
                val domain: String? = "",
                @SerializedName("id")
                val id: String? = "",
                @SerializedName("image_shop")
                val imageShop: ImageShop?,
                @SerializedName("location")
                val location: String? = "",
                @SerializedName("name")
                val name: String? = "",
                @SerializedName("power_merchant")
                val powerMerchant: Boolean = false,
                @SerializedName("power_merchant_badge")
                val powerMerchantBadge: Boolean = false,
                @SerializedName("shop_is_official")
                val shopIsOfficial: Boolean = false,
                @SerializedName("tagline")
                val tagline: String? = ""
            ) {
                data class ImageShop(
                    @SerializedName("cover")
                    val cover: String? = "",
                    @SerializedName("cover_ecs")
                    val coverEcs: String? = "",
                    @SerializedName("s_ecs")
                    val sEcs: String? = "",
                    @SerializedName("s_url")
                    val sUrl: String? = "",
                    @SerializedName("xs_ecs")
                    val xsEcs: String? = "",
                    @SerializedName("xs_url")
                    val xsUrl: String? = ""
                )
            }
        }
    }

    data class Header(
        @SerializedName("meta")
        val meta: Meta?,
        @SerializedName("process_time")
        val processTime: Double?,
        @SerializedName("total_data")
        val totalData: Int?
    ) {
        data class Meta(
            @SerializedName("ab_test")
            val abTest: String? = "",
            @SerializedName("display")
            val display: String? = ""
        )
    }

    data class Status(
        @SerializedName("error_code")
        val errorCode: Int?,
        @SerializedName("message")
        val message: String? = ""
    )
}