package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName

data class GetAddOnByProductRequest (
    @SerializedName("AddOnRequest")
    var addOnRequest: List<AddOnRequest> = listOf(),

    @SerializedName("Source")
    var source: Source = Source()
)

data class Additional (
    @SerializedName("ProductID")
    var productID: String? = null,

    @SerializedName("CategoryID")
    var categoryID: String = "",

    @SerializedName("ShopID")
    var shopID: String = "",

    @SerializedName("Quantity")
    var quantity: Long = 0L,

    @SerializedName("Price")
    var price: Long = 0L,

    @SerializedName("DiscountedPrice")
    var discountedPrice: Long = 0L,

    @SerializedName("Condition")
    var condition: String = ""
)

data class TypeFilters (
    @SerializedName("Type")
    var type: String = "",

    @SerializedName("Quantity")
    var quantity: Int = 0
)

data class AddOnRequest (
    @SerializedName("ProductID")
    var productId: String = "",

    @SerializedName("WarehouseID")
    var warehouseId: String = "",

    @SerializedName("AddOnLevel")
    var addOnLevel: String = "",

    @SerializedName("Additional")
    var additional: Additional = Additional(),

    @SerializedName("TypeFilters")
    var typeFilters: List<TypeFilters> = listOf()
)

data class Source (
    @SerializedName("Squad")
    var squad: String = "",

    @SerializedName("Usecase")
    var usecase: String = ""
)
