package com.tokopedia.shop_widget.mvc_locked_to_product.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MvcLockedToProductRequest(
    @SerializedName("promoID")
    val shopID: String = "",
    @SerializedName("promoID")
    val promoID: String = "",
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("perPage")
    val perPage: Int = 0,
    @SerializedName("sortBy")
    val sortBy: String,
    @SerializedName("districtID")
    val districtID: String,
    @SerializedName("cityID")
    val cityID: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String
)