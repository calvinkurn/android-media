package com.tokopedia.universal_sharing.view.model

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("ProductID")
    var productID: String? = null,

    @SerializedName("CatLevel1")
    var catLevel1: String? = null,

    @SerializedName("CatLevel2")
    var catLevel2: String? = null,

    @SerializedName("CatLevel3")
    var catLevel3: String? = null,

    @SerializedName("ProductPrice")
    var productPrice: String? = null,

    @SerializedName("MaxProductPrice")
    var maxProductPrice: String? = null,

    @SerializedName("ProductStatus")
    var productStatus: String? = null,
)

data class Shop (
    @SerializedName("ShopID")
    var shopID: String? = null,

    @SerializedName("ShopStatus")
    var shopStatus: Int = 0,

    @SerializedName("IsOS")
    var isOS: Boolean = false,

    @SerializedName("IsPM")
    var isPM: Boolean = false
    )

data class AffiliatePDPInput (
    @SerializedName("PageType")
    var pageType: String? = null,

    @SerializedName("Product")
    var product: Product? = null,

    @SerializedName("Shop")
    var shop: Shop? = null
)
