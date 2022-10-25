package com.tokopedia.universal_sharing.view.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.universal_sharing.tracker.PageType

data class Product (
    @SerializedName("ProductID")
    var productID: String? = "",

    @SerializedName("CatLevel1")
    var catLevel1: String? = "",

    @SerializedName("CatLevel2")
    var catLevel2: String? = "",

    @SerializedName("CatLevel3")
    var catLevel3: String? = "",

    @SerializedName("ProductPrice")
    var productPrice: String? = "",

    @SerializedName("MaxProductPrice")
    var maxProductPrice: String? = "",

    @SerializedName("ProductStatus")
    var productStatus: String? = "",
)

data class Shop (
    @SerializedName("ShopID")
    var shopID: String? = "",

    @SerializedName("ShopStatus")
    var shopStatus: Int? = 0,

    @SerializedName("IsOS")
    var isOS: Boolean = false,

    @SerializedName("IsPM")
    var isPM: Boolean = false
    )

data class PageDetail(
        @SerializedName("PageType")
        val pageType: String = "",

        @SerializedName("PageID")
        val pageId: String = "",

        @SerializedName("SiteID")
        val siteId: String = "",

        @SerializedName("VerticalID")
        val verticalId: String = ""
)

data class AffiliatePDPInput (
    @SerializedName("PageType")
    var pageType: String? = "",

    @SerializedName("Product")
    var product: Product? = null,

    @SerializedName("Shop")
    var shop: Shop? = null,

    @SerializedName("PageDetail")
    var pageDetail: PageDetail? = null
) {
    /** get id based on page type
     * [PageType.PDP] return [Product.productID]
     * [PageType.PDP] return [Shop.shopID]
     */
    fun getIdFactory(): String {
        return when (pageType) {
            PageType.PDP.value -> product?.productID ?: ""
            PageType.SHOP.value -> shop?.shopID ?: ""
            else -> ""
        }
    }
}
