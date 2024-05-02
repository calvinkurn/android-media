package com.tokopedia.search.result.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.search.result.domain.model.SearchProductModel.Banner
import com.tokopedia.search.result.domain.model.SearchProductModel.Redirection
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchProductHeaderMeta
import com.tokopedia.search.result.domain.model.SearchProductModel.Suggestion
import com.tokopedia.search.result.domain.model.SearchProductModel.Ticker
import com.tokopedia.search.result.domain.model.SearchProductModel.Violation

data class SearchProductV5(
    @SerializedName("header")
    @Expose
    val header: Header = Header(),

    @SerializedName("data")
    @Expose
    val data: Data = Data(),
) {

    val relatedKeyword: String
        get() = data.related.relatedKeyword

    val backendFilters: String
        get() = header.backendFilters

    data class Header(
        @SerializedName("totalData")
        @Expose
        val totalData: Long = 0L,

        @SerializedName("responseCode")
        @Expose
        val responseCode: String = "0",

        @SerializedName("keywordProcess")
        @Expose
        val keywordProcess: String = "",

        @SerializedName("keywordIntention")
        @Expose
        val keywordIntention: Int = 0,

        @SerializedName("componentID")
        @Expose
        val componentID: String = "",

        @SerializedName("meta")
        @Expose
        val meta: SearchProductHeaderMeta = SearchProductHeaderMeta(),

        @SerializedName("isQuerySafe")
        @Expose
        val isQuerySafe: Boolean = true,

        @SerializedName("additionalParams")
        @Expose
        val additionalParams: String = "",

        @SerializedName("autocompleteApplink")
        @Expose
        val autocompleteApplink: String = "",

        @SerializedName("backendFilters")
        @Expose
        val backendFilters: String = "",

        @SerializedName("backendFiltersToggle")
        @Expose
        val backendFiltersToggle: String = "",
    )

    data class Data(
        @SerializedName("totalDataText")
        @Expose
        val totalDataText: String = "",

        @SerializedName("banner")
        @Expose
        val banner: Banner = Banner(),

        @SerializedName("redirection")
        @Expose
        val redirection: Redirection = Redirection(),

        @SerializedName("related")
        @Expose
        val related: Related = Related(),

        @SerializedName("suggestion")
        @Expose
        val suggestion: Suggestion = Suggestion(),

        @SerializedName("ticker")
        @Expose
        val ticker: Ticker = Ticker(),

        @SerializedName("violation")
        @Expose
        val violation: Violation = Violation(),

        @SerializedName("products")
        @Expose
        val productList: List<Product> = listOf(),
    ) {

        data class Related(
            @SerializedName("relatedKeyword")
            @Expose
            val relatedKeyword: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

            @SerializedName("trackingOption")
            @Expose
            val trackingOption: Int = 0,

            @SerializedName("otherRelated")
            @Expose
            val otherRelatedList: List<OtherRelated> = listOf(),
        ) {

            data class OtherRelated(
                @SerializedName("keyword")
                @Expose
                val keyword: String = "",

                @SerializedName("url")
                @Expose
                val url: String = "",

                @SerializedName("applink")
                @Expose
                val applink: String = "",

                @SerializedName("componentID")
                @Expose
                val componentID: String = "",

                @SerializedName("products")
                @Expose
                val productList: List<Product> = listOf(),
            ) {

                data class Product(
                    @SerializedName("id")
                    @Expose
                    val id: String = "",

                    @SerializedName("name")
                    @Expose
                    val name: String = "",

                    @SerializedName("url")
                    @Expose
                    val url: String = "",

                    @SerializedName("applink")
                    @Expose
                    val applink: String = "",

                    @SerializedName("mediaURL")
                    @Expose
                    val mediaURL: MediaURL = MediaURL(),

                    @SerializedName("shop")
                    @Expose
                    val shop: Shop = Shop(),

                    @SerializedName("badge")
                    @Expose
                    val badge: Badge = Badge(),

                    @SuppressLint("Invalid Data Type")
                    @SerializedName("price")
                    @Expose
                    val price: Price = Price(),

                    @SerializedName("freeShipping")
                    @Expose
                    val freeShipping: FreeShipping = FreeShipping(),

                    @SerializedName("labelGroups")
                    @Expose
                    val labelGroupList: List<LabelGroup> = listOf(),

                    @SerializedName("rating")
                    @Expose
                    val rating: String = "",

                    @SerializedName("wishlist")
                    @Expose
                    val isWishlisted: Boolean = false,

                    @SerializedName("ads")
                    @Expose
                    val ads: Ads = Ads(),

                    @SerializedName("meta")
                    @Expose
                    val meta: Meta = Meta(),
                ) {

                    fun isOrganicAds(): Boolean = ads.id.isNotEmpty()

                    data class Meta(
                        @SerializedName("warehouseID")
                        @Expose
                        val warehouseID: String = "",

                        @SerializedName("componentID")
                        @Expose
                        val componentID: String = "",

                        @SerializedName("isImageBlurred")
                        @Expose
                        val isImageBlurred: Boolean = false,

                        @SerializedName("parentID")
                        @Expose
                        val parentId: String =  ""
                    )
                }
            }
        }

        data class Product(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("mediaURL")
            @Expose
            val mediaURL: MediaURL = MediaURL(),

            @SerializedName("shop")
            @Expose
            val shop: Shop = Shop(),

            @SerializedName("badge")
            @Expose
            val badge: Badge = Badge(),

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: Price = Price(),

            @SerializedName("freeShipping")
            @Expose
            val freeShipping: FreeShipping = FreeShipping(),

            @SerializedName("labelGroups")
            @Expose
            val labelGroupList: List<LabelGroup> = listOf(),

            @SerializedName("labelGroupsVariant")
            @Expose
            val labelGroupVariantList: List<LabelGroupVariant> = listOf(),

            @SerializedName("category")
            @Expose
            val category: Category = Category(),

            @SerializedName("rating")
            @Expose
            val rating: String = "",

            @SerializedName("wishlist")
            @Expose
            val wishlist: Boolean = false,

            @SerializedName("ads")
            @Expose
            val ads: Ads = Ads(),

            @SerializedName("meta")
            @Expose
            val meta: Meta = Meta(),
        ) {

            fun isOrganicAds() = ads.isOrganicAds()

            data class Meta(
                @SerializedName("parentID")
                @Expose
                val parentID: String = "",

                @SerializedName("warehouseID")
                @Expose
                val warehouseID: String = "",

                @SerializedName("isPortrait")
                @Expose
                val isPortrait: Boolean = false,

                @SerializedName("isImageBlurred")
                @Expose
                val isImageBlurred: Boolean = false,
            )

            data class LabelGroupVariant(
                @SerializedName("title")
                @Expose
                val title: String = "",

                @SerializedName("type")
                @Expose
                val type: String = "",

                @SerializedName("typeVariant")
                @Expose
                val typeVariant: String = "",

                @SerializedName("hexColor")
                @Expose
                val hexColor: String = "",
            )

            data class Category(
                @SuppressLint("Invalid Data Type")
                @SerializedName("id")
                @Expose
                val id: Int = 0,

                @SerializedName("name")
                @Expose
                val name: String = "",

                @SerializedName("breadcrumb")
                @Expose
                val breadcrumb: String = "",
            )
        }

        data class MediaURL(
            @SerializedName("image")
            @Expose
            val image: String = "",

            @SerializedName("image300")
            @Expose
            val image300: String = "",

            @SerializedName("image700")
            @Expose
            val image700: String = "",

            @SerializedName("videoCustom")
            @Expose
            val videoCustom: String = "",
        )

        data class Shop(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("city")
            @Expose
            val city: String = "",

            @SerializedName("tier")
            @Expose
            val tier: Int = 0,
        )

        data class Badge(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",
        )

        data class Price(
            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("number")
            @Expose
            val number: Int = 0,

            @SerializedName("range")
            @Expose
            val range: String = "",

            @SerializedName("original")
            @Expose
            val original: String = "",

            @SerializedName("discountPercentage")
            @Expose
            val discountPercentage: Float = 0F,
        )

        data class FreeShipping(
            @SerializedName("url")
            @Expose
            val url: String = "",
        )

        data class LabelGroup(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("position")
            @Expose
            val position: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("styles")
            @Expose
            val styleList: List<Style> = listOf(),
        )

        data class Style(
            @SerializedName("key")
            @Expose
            val key: String = "",

            @SerializedName("value")
            @Expose
            val value: String = "",
        )

        data class Ads(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("productClickURL")
            @Expose
            val productClickURL: String = "",

            @SerializedName("productViewURL")
            @Expose
            val productViewURL: String = "",

            @SerializedName("productWishlistURL")
            @Expose
            val productWishlistURL: String = "",

            @SerializedName("tag")
            @Expose
            val tag: Int = 0,

            @SerializedName("creativeID")
            val creativeID: String = "",

            @SerializedName("logExtra")
            val logExtra: String = "",
        ) {
            fun isOrganicAds() = id.isNotEmpty()
        }
    }
}
