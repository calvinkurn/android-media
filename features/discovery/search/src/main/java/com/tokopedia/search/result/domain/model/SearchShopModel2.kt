package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.SerializedName

data class SearchShopModel2(
    @SerializedName("source")
    var source: String = "",

    @SerializedName("total_shop")
    var totalShop: Int = 0,

    @SerializedName("search_url")
    var searchUrl: String = "",

    @SerializedName("paging")
    var paging: Paging = Paging(),

    @SerializedName("tab_name")
    var tabName: String = "",

    @SerializedName("shops")
    var shopList: List<ShopItem> = listOf(),

    @SerializedName("top_seller_data")
    var topSellerData: List<ShopItem> = listOf(),

    @SerializedName("top_official_seller_data")
    var topOfficialSellerData: List<ShopItem> = listOf()
) {

    data class Paging(
        @SerializedName("uri_next")
        var uriNext: String = "",

        @SerializedName("uri_previous")
        var uriPrevious: String = ""
    )

    data class ShopItem(
        @SerializedName("shop_id")
        var id: String = "",

        @SerializedName("shop_name")
        var name: String = "",

        @SerializedName("shop_domain")
        var domain: String= "",

        @SerializedName("shop_url")
        var url: String = "",

        @SerializedName("shop_applink")
        var applink: String = "",

        @SerializedName("shop_image")
        var image: String = "",

        @SerializedName("shop_image_300")
        var image300: String = "",

        @SerializedName("shop_description")
        var description: String = "",

        @SerializedName("shop_tag_line")
        var tagLine: String = "",

        @SerializedName("shop_location")
        var location: String = "",

        @SerializedName("shop_total_transaction")
        var totalTransaction: String = "",

        @SerializedName("shop_total_favorite")
        var totalFavorite: String = "",

        @SerializedName("shop_gold_shop")
        var goldShop: Int = 0,

        @SerializedName("shop_is_owner")
        var isOwner: Int = 0,

        @SerializedName("shop_rate_speed")
        var rateSpeed: Int = 0,

        @SerializedName("shop_rate_accuracy")
        var rateAccuracy: Int = 0,

        @SerializedName("shop_rate_service")
        var rateService: Int = 0,

        @SerializedName("shop_status")
        var status: Int = 0,

        // TODO:: Product list here

        // TODO:: Voucher here

        @SerializedName("shop_lucky")
        var lucky: String = "",

        @SerializedName("reputation_image_uri")
        var reputationImageUri: String = "",

        @SerializedName("reputation_score")
        var reputationScore: Int = 0,

        @SerializedName("is_official")
        var isOfficial: Boolean = false,

        @SerializedName("ga_key")
        var gaKey: String = ""
    ) {

        data class Product(
            @SerializedName("id")
            var id: Int = 0,

            @SerializedName("name")
            var name: String = "",

            @SerializedName("url")
            val url: String = "",

            @SerializedName("applink")
            val applink: String = "",

            @SerializedName("price")
            val price: Int = 0,

            @SerializedName("image_url")
            val imageUrl: String = ""
        )
    }
}