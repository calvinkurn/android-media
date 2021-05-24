package com.tokopedia.search.result.shop.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.topads.sdk.domain.model.CpmModel

internal data class SearchShopModel(
        @SerializedName("aceSearchShop")
        @Expose
        val aceSearchShop: AceSearchShop = AceSearchShop(),

        @SerializedName("headlineAds")
        @Expose
        val cpmModel: CpmModel = CpmModel(),

        @SerializedName("quickFilter")
        @Expose
        var quickFilter: FilterSort = FilterSort()
) {
    fun hasShopList() = aceSearchShop.shopList.isNotEmpty()

    fun hasRecommendationShopList() = aceSearchShop.topShopList.isNotEmpty()

    fun getQuickFilterList() = quickFilter.data.filter

    data class AceSearchShop(
            @SerializedName("source")
            @Expose
            val source: String = "",

            @SerializedName("total_shop")
            @Expose
            val totalShop: Int = 0,

            @SerializedName("search_url")
            @Expose
            val searchUrl: String = "",

            @SerializedName("paging")
            @Expose
            val paging: Paging = Paging(),

            @SerializedName("tab_name")
            @Expose
            val tabName: String = "",

            @SerializedName("suggestion")
            @Expose
            val suggestion: Suggestion = Suggestion(),

            @SerializedName("shops")
            @Expose
            val shopList: List<ShopItem> = listOf(),

            @SerializedName("top_shop")
            @Expose
            val topShopList: List<ShopItem> = listOf(),
    ) {

        data class Paging(
                @SerializedName("uri_next")
                @Expose
                val uriNext: String = "",

                @SerializedName("uri_previous")
                @Expose
                val uriPrevious: String = ""
        )

        data class Suggestion(
                @SerializedName("currentKeyword")
                @Expose
                val currentKeyword: String = "",

                @SerializedName("query")
                @Expose
                val query: String = "",

                @SerializedName("text")
                @Expose
                val text: String = "",
        )

        data class ShopItem(
                @SerializedName("shop_id")
                @Expose
                val id: String = "",

                @SerializedName("shop_name")
                @Expose
                val name: String = "",

                @SerializedName("shop_domain")
                @Expose
                val domain: String = "",

                @SerializedName("shop_url")
                @Expose
                val url: String = "",

                @SerializedName("shop_applink")
                @Expose
                val applink: String = "",

                @SerializedName("shop_image")
                @Expose
                val image: String = "",

                @SerializedName("shop_image_300")
                @Expose
                val image300: String = "",

                @SerializedName("shop_description")
                @Expose
                val description: String = "",

                @SerializedName("shop_tag_line")
                @Expose
                val tagLine: String = "",

                @SerializedName("shop_location")
                @Expose
                val location: String = "",

                @SerializedName("shop_total_transaction")
                @Expose
                val totalTransaction: String = "",

                @SerializedName("shop_total_favorite")
                @Expose
                val totalFavorite: String = "",

                @SerializedName("shop_gold_shop")
                @Expose
                val goldShop: Int = 0,

                @SerializedName("shop_is_owner")
                @Expose
                val isOwner: Int = 0,

                @SerializedName("shop_rate_speed")
                @Expose
                val rateSpeed: Int = 0,

                @SerializedName("shop_rate_accuracy")
                @Expose
                val rateAccuracy: Int = 0,

                @SerializedName("shop_rate_service")
                @Expose
                val rateService: Int = 0,

                @SerializedName("shop_status")
                @Expose
                val status: Int = 0,

                @SerializedName("products")
                @Expose
                val productList: List<ShopItemProduct> = listOf(),

                @SerializedName("voucher")
                @Expose
                val voucher: ShopItemVoucher = ShopItemVoucher(),

                @SerializedName("shop_lucky")
                @Expose
                val lucky: String = "",

                @SerializedName("reputation_image_uri")
                @Expose
                val reputationImageUri: String = "",

                @SerializedName("reputation_score")
                @Expose
                val reputationScore: Int = 0,

                @SerializedName("is_official")
                @Expose
                val isOfficial: Boolean = false,

                @SerializedName("ga_key")
                @Expose
                val gaKey: String = "",

                @SerializedName("is_pm_pro")
                @Expose
                val isPMPro: Boolean = false,
        ) {

            data class ShopItemProduct(
                    @SerializedName("id")
                    @Expose
                    val id: Int = 0,

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("url")
                    @Expose
                    val url: String = "",

                    @SerializedName("applink")
                    @Expose
                    val applink: String = "",

                    @SerializedName("price")
                    @Expose
                    val price: Int = 0,

                    @SerializedName("price_format")
                    @Expose
                    val priceFormat: String = "",

                    @SerializedName("image_url")
                    @Expose
                    val imageUrl: String = ""
            )

            data class ShopItemVoucher(
                    @SerializedName("free_shipping")
                    @Expose
                    val freeShipping: Boolean = false,

                    @SerializedName("cashback")
                    @Expose
                    val cashback: ShopItemVoucherCashback = ShopItemVoucherCashback()
            ) {

                data class ShopItemVoucherCashback(
                        @SerializedName("cashback_value")
                        @Expose
                        val cashbackValue: Int = 0,

                        @SerializedName("is_percentage")
                        @Expose
                        val isPercentage: Boolean = false
                )
            }
        }
    }

        data class FilterSort(
                @SerializedName("data")
                @Expose
                var data: DataValue = DataValue()
        )

        fun getFilterList() = quickFilter.data.filter
}