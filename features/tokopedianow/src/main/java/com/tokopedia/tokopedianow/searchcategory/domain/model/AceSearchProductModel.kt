package com.tokopedia.tokopedianow.searchcategory.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst

data class AceSearchProductModel(
        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct()
) {
    private companion object {
        const val LABEL_STATUS = "status"
        const val TRANSPARENT_BLACK = "transparentBlack"
    }

    data class SearchProduct (
            @SerializedName("header")
            @Expose
            val header: SearchProductHeader = SearchProductHeader(),

            @SerializedName("data")
            @Expose
            val data: SearchProductData = SearchProductData()
    ) {

        fun getResponseCode() = header.responseCode

        fun getAlternativeKeyword(): String {
            val alternativeKeywordList = generateAlternativeKeywordList()

            return if (alternativeKeywordList.isEmpty()) SearchCategoryTrackingConst.Misc.NONE
            else alternativeKeywordList.joinToString()
        }

        private fun generateAlternativeKeywordList(): List<String> {
            val alternativeKeywordList = mutableListOf<String>()

            if (isUseSuggestionKeyword())
                addAllSuggestionKeyword(alternativeKeywordList)
            else if (isUseRelatedKeyword())
                addAllRelatedKeyword(alternativeKeywordList)

            return alternativeKeywordList
        }

        private fun isUseSuggestionKeyword() = getResponseCode() == "7"

        private fun addAllSuggestionKeyword(alternativeKeywordList: MutableList<String>) {
            val suggestion = data.suggestion.suggestion

            if (suggestion.isNotBlank())
                alternativeKeywordList.add(suggestion)
        }

        private fun isUseRelatedKeyword() =
            listOf("3", "4", "5", "6").contains(getResponseCode())

        private fun addAllRelatedKeyword(alternativeKeywordList: MutableList<String>) {
            val related = data.related

            val relatedKeyword = related.relatedKeyword
            if (relatedKeyword.isNotBlank())
                alternativeKeywordList.add(relatedKeyword)

            val otherRelatedKeyword = related.otherRelatedList
                .map { it.keyword }
                .filter { it.isNotBlank() }
            alternativeKeywordList.addAll(otherRelatedKeyword)
        }
    }

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
            val keywordProcess: String = "0",

            @SerializedName("meta")
            @Expose
            val meta: Meta = Meta()
    )

    data class Meta(
        @SerializedName("categoryId")
        val categoryId: String = "0"
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

            @SerializedName("banner")
            @Expose
            val banner: Banner = Banner(),

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
    )

        data class Related(
            @SerializedName("relatedKeyword")
            @Expose
            val relatedKeyword: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

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

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: Int = 0,

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("priceStr")
            @Expose
            val priceString: String = "",

            @SerializedName("wishlist")
            @Expose
            val isWishlisted: Boolean = false,

            @SerializedName("shop")
            @Expose
            val shop: OtherRelatedProductShop = OtherRelatedProductShop(),

            @SerializedName("ratingAverage")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("labelGroups")
            @Expose
            val labelGroupList: List<ProductLabelGroup> = listOf(),

            @SerializedName("minOrder")
            @Expose
            val minOrder: Int = 1,

            @SerializedName("maxOrder")
            @Expose
            val maxOrder: Int = 0,

            @SerializedName("stock")
            @Expose
            val stock: Int = 0,
        )

        data class OtherRelatedProductShop(
            @SerializedName("id")
            @Expose
            val id: String = "",
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

    data class Banner(
            @SerializedName("position")
            @Expose
            val position: Int = -1,

            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",
    )

    data class Product(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

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

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceInt")
            @Expose
            val priceInt: Double = 0.0,

            @SerializedName("priceRange")
            @Expose
            val priceRange: String = "",

            @SerializedName("categoryBreadcrumb")
            @Expose
            val categoryBreadcrumb: String = "",

            @SerializedName("categoryName")
            @Expose
            val categoryName: String = "",

            @SerializedName("ratingAverage")
            @Expose
            val ratingAverage: String = "",

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

            @SerializedName("source_engine")
            @Expose
            val sourceEngine: String = "",

            @SerializedName("labelGroups")
            @Expose
            val labelGroupList: List<ProductLabelGroup> = listOf(),

            @SerializedName("labelGroupVariant")
            @Expose
            val labelGroupVariantList: List<ProductLabelGroupVariant> = listOf(),

            @SerializedName("badges")
            @Expose
            val badgeList: List<ProductBadge> = listOf(),

            @SerializedName("wishlist")
            @Expose
            val isWishlist: Boolean = false,

            @SerializedName("minOrder")
            @Expose
            val minOrder: Int = 1,

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("childs")
            @Expose
            val childs: List<String> = listOf(),

            @SerializedName("category_id")
            @Expose
            val categoryId: String = "",

            @SerializedName("parentId")
            @Expose
            val parentId: String = "",

            @SerializedName("maxOrder")
            @Expose
            val maxOrder: Int = 0,

            @SerializedName("stock")
            @Expose
            val stock: Int = 0
    ) {
        private fun getOosLabelGroup() = labelGroupList.firstOrNull { (stock < minOrder || stock == Int.ZERO) && it.isStatusPosition() && it.isTransparentBlackColor() }

        fun isOos() = getOosLabelGroup() != null
    }

    data class ProductShop(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("city")
            @Expose
            val city: String = "",

            @SerializedName("rating_average")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("isOfficial")
            @Expose
            val isOfficial: Boolean = false,

            @SerializedName("isPowerBadge")
            @Expose
            val isPowerBadge: Boolean = false,

            @SerializedName("url")
            @Expose
            val url: String = ""
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
            val type: String = "",

            @SerializedName("url")
            @Expose
            val url: String = ""
    ) {
        fun isStatusPosition() = position == LABEL_STATUS

        fun isTransparentBlackColor() = type == TRANSPARENT_BLACK
    }

    data class ProductLabelGroupVariant(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("type_variant")
            @Expose
            val typeVariant: String = "",

            @SerializedName("hex_color")
            @Expose
            val hexColor: String = ""
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
}
