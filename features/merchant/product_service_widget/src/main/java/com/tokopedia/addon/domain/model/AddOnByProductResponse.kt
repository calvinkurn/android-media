package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName


data class AddOnByProductResponse (
    @SerializedName("ProductID")
    var productID: String = "",

    @SerializedName("WarehouseID")
    var warehouseID: String = "",

    @SerializedName("AddOnLevel")
    var addOnLevel: String = "",

    @SerializedName("Addons")
    var addons: List<Addon> = listOf()
)
