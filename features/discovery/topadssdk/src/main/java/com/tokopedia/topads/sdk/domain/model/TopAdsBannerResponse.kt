package com.tokopedia.topads.sdk.domain.model


import com.google.gson.annotations.SerializedName

data class TopAdsBannerResponse(
    @SerializedName("topadsDisplayBannerAdsV3")
    val topadsDisplayBannerAdsV3: TopadsDisplayBannerAdsV3
) {
    data class TopadsDisplayBannerAdsV3(
        @SerializedName("data")
        val bannerListData: List<BannerListData>?,
        @SerializedName("header")
        val header: Header?
    ) {
        data class BannerListData(
            @SerializedName("ad_click_url")
            val adClickUrl: String? = "",
            @SerializedName("ad_ref_key")
            val adRefKey: String? = "",
            @SerializedName("ad_view_url")
            val adViewUrl: String? = "",
            @SerializedName("banner")
            val banner: Banner?,
            @SerializedName("id")
            val id: String? = "",
            @SerializedName("applinks")
            val applinks: String? = ""
        ) {
            data class Banner(
                @SerializedName("Images")
                val images: List<Image>?,
                @SerializedName("Name")
                val name: String? = "",
                @SerializedName("Position")
                val position: Int = 0,
                @SerializedName("LayoutType")
                val layoutType: String = "",
                @SerializedName("Shop")
                val shop: Shop?
            ) {
                data class Image(
                    @SerializedName("Dimension")
                    val dimension: Dimension?,
                    @SerializedName("Url")
                    val url: String? = ""
                ) {
                    data class Dimension(
                        @SerializedName("Height")
                        val height: Int = 0,
                        @SerializedName("Id")
                        val id: String? = "",
                        @SerializedName("Width")
                        val width: Int = 0
                    )
                }

                data class Shop(
                    @SerializedName("IsPowerMerchant")
                    val isPowerMerchant: Boolean = false,
                    @SerializedName("IsPowerMerchantBadge")
                    val isPowerMerchantBadge: Boolean = false,
                    @SerializedName("ShopCityName")
                    val shopCityName: String? = "",
                    @SerializedName("ShopDomain")
                    val shopDomain: String? = "",
                    @SerializedName("ShopID")
                    val shopID: Int = 0,
                    @SerializedName("ShopImage")
                    val shopImage: ImageShop?,
                    @SerializedName("ShopIsOfficial")
                    val shopIsOfficial: Boolean = false,
                    @SerializedName("ShopName")
                    val shopName: String? = "",
                    @SerializedName("ShopTagline")
                    val shopTagline: String? = ""
                )
            }
        }

        data class Header(
            @SerializedName("pagination")
            val pagination: Pagination?,
            @SerializedName("total_data")
            val totalData: Int = 0
        ) {
            class Pagination(
                @SerializedName("current_page")
                val currentPage: String? = "",
                @SerializedName("kind")
                val kind: String? = "",
                @SerializedName("next_page_token")
                val nextPageToken: String? = ""
            )
        }
    }
}