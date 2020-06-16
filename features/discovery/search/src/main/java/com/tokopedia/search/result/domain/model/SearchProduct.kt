package com.tokopedia.search.result.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchProduct (
        @SerializedName("header")
        @Expose
        val header: SearchProductHeader = SearchProductHeader(),

        @SerializedName("data")
        @Expose
        val data: SearchProductData = SearchProductData()
)

data class SearchProductHeader(
        @SerializedName("totalData")
        @Expose
        val totalData: Int = 0,

        @SerializedName("totalDataText")
        @Expose
        val totalDataText: String = "",

        @SerializedName("defaultView")
        @Expose
        val defaultView: Int = 0,

        @SerializedName("responseCode")
        @Expose
        val responseCode: String = "0",

        @SerializedName("errorMessage")
        @Expose
        val errorMessage: String = "",

        @SerializedName("additionalParams")
        @Expose
        val additionalParams: String = "",

        @SerializedName("keywordProcess")
        @Expose
        val keywordProcess: String = "0"
)

data class SearchProductData(
        @SerializedName("isQuerySafe")
        @Expose
        val isQuerySafe: Boolean = true,

        @SerializedName("autocompleteApplink")
        @Expose
        val autocompleteApplink: String = "",

        @SerializedName("redirection")
        @Expose
        val redirection: Redirection = Redirection(),

        @SerializedName("ticker")
        @Expose
        val ticker: Ticker = Ticker(),

        @SerializedName("related")
        @Expose
        val related: Related = Related(),

        @SerializedName("suggestion")
        @Expose
        val suggestion: Suggestion = Suggestion(),

        @SerializedName("products")
        @Expose
        val productList: List<Product> = listOf()
)

data class Redirection(
        @SerializedName("redirectApplink")
        @Expose
        val redirectApplink: String = ""
)

data class Ticker(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("query")
        @Expose
        val query: String = "",

        @SerializedName("typeId")
        @Expose
        val typeId: Int = 0
)

data class Related(
        @SerializedName("relatedKeyword")
        @Expose
        val relatedKeyword: String = "",

        @SerializedName("otherRelated")
        @Expose
        val otherRelatedList: List<OtherRelated> = listOf()
)

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

        @SerializedName("product")
        @Expose
        val productList: List<OtherRelatedProduct> = listOf()
)

data class OtherRelatedProduct(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("countReview")
        @Expose
        val countReview: Int = 0,

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("priceStr")
        @Expose
        val priceString: String = ""
)

data class Suggestion(
        @SerializedName("suggestion")
        @Expose
        val suggestion: String = "",

        @SerializedName("query")
        @Expose
        val query: String = "",

        @SerializedName("text")
        @Expose
        val text: String = ""
)

data class Product(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("ads")
        @Expose
        val ads: ProductAds = ProductAds(),

        @SerializedName("shop")
        @Expose
        val shop: ProductShop = ProductShop(),

        @SerializedName("freeOngkir")
        @Expose
        val freeOngkir: ProductFreeOngkir = ProductFreeOngkir(),

        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",

        @SerializedName("imageUrl300")
        @Expose
        val imageUrl300: String = "",

        @SerializedName("imageUrl700")
        @Expose
        val imageUrl700: String = "",

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("priceInt")
        @Expose
        val priceInt: Int = 0,

        @SerializedName("priceRange")
        @Expose
        val priceRange: String = "",

        @SerializedName("categoryBreadcrumb")
        @Expose
        val categoryBreadcrumb: String = "",

        @SerializedName("categoryId")
        @Expose
        val categoryId: Int = 0,

        @SerializedName("categoryName")
        @Expose
        val categoryName: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("ratingAverage")
        @Expose
        val ratingAverage: String = "",

        @SerializedName("countReview")
        @Expose
        val countReview: Int = 0,

        @SerializedName("originalPrice")
        @Expose
        val originalPrice: String = "",

        @SerializedName("discountPercentage")
        @Expose
        val discountPercentage: Int = 0,

        @SerializedName("warehouseIdDefault")
        @Expose
        val warehouseIdDefault: String = "",

        @SerializedName("boosterList")
        @Expose
        val boosterList: String = "",

        @SerializedName("labelGroups")
        @Expose
        val labelGroupList: List<ProductLabelGroup> = listOf(),

        @SerializedName("badges")
        @Expose
        val badgeList: List<ProductBadge> = listOf(),

        @SerializedName("wishlist")
        @Expose
        val isWishlist: Boolean = false
) {

    fun isOrganicAds(): Boolean = ads.id.isNotEmpty()
}

data class ProductAds(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("productClickUrl")
        @Expose
        val productClickUrl: String = "",

        @SerializedName("productWishlistUrl")
        @Expose
        val productWishlistUrl: String = "",

        @SerializedName("productViewUrl")
        @Expose
        val productViewUrl: String = ""
)

data class ProductShop(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("city")
        @Expose
        val city: String = ""
)

data class ProductFreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("imgUrl")
        @Expose
        val imageUrl: String = ""
)

data class ProductLabelGroup(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("position")
        @Expose
        val position: String = "",

        @SerializedName("type")
        @Expose
        val type: String = ""
)

data class ProductBadge(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",

        @SerializedName("show")
        @Expose
        val isShown: Boolean = false
)