package com.tokopedia.catalog.model.raw

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalog.adapter.factory.CatalogTypeFactory
import com.tokopedia.common_category.model.productModel.BadgesItem
import com.tokopedia.topads.sdk.domain.model.ImpressHolder
import kotlinx.android.parcel.Parcelize
import java.util.*

data class CatalogSearchProductResponse(
        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct()

) {

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

            @SerializedName("totalData")
            @Expose
            var totalData: Int = 0,

            @SerializedName("isQuerySafe")
            @Expose
            val isQuerySafe: Boolean = true,

            @SerializedName("autocompleteApplink")
            @Expose
            val autocompleteApplink: String = "",

            @SerializedName("products")
            @Expose
            val catalogProductItemList: ArrayList<CatalogProductItem?> = ArrayList()
    )
}

@Parcelize
data class CatalogProductItem(
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
        val priceString: String = "",

        @SerializedName("categoryBreadcrumb")
        @Expose
        val categoryBreadcrumb: String = "",

        @SerializedName("categoryId")
        @Expose
        val categoryId: Int = 0,

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
        val badgeList: List<BadgesItem> = listOf(),

        @SerializedName("minOrder")
        @Expose
        val minOrder: Int = 1,

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("wishlist")
        @Expose
        var wishlist: Boolean = false,

        @SerializedName("isWishListEnabled")
        @Expose
        var isWishListEnabled: Boolean = true,

        @SerializedName("productImpTrackingUrl")
        @Expose
        var productImpTrackingUrl: String = "",

        @SerializedName("dimension")
        @Expose
        var dimension: String? = null,

        @SerializedName("adapterPosition")
        @Expose
        var adapter_position: Int = 0,

        @SerializedName("isTopAds")
        @Expose
        var isTopAds: Boolean = false,

        @SerializedName("productClickTrackingUrl")
        @Expose
        var productClickTrackingUrl: String = "",

        @SerializedName("productWishlistTrackingUrl")
        @Expose
        var productWishlistTrackingUrl: String = "",

        @SerializedName("countReview")
        @Expose
        var countReview: Int = 0

) : ImpressHolder() , Parcelable , Visitable<CatalogTypeFactory> {

    override fun type(typeFactory: CatalogTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}

@Parcelize
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
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
data class ProductFreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("imgUrl")
        @Expose
        val imageUrl: String = ""
) : Parcelable

@Parcelize
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
) : Parcelable

@Parcelize
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
) : Parcelable