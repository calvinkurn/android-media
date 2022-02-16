package com.tokopedia.shop_widget.mvc_locked_to_product.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MvcLockedToProductRequest(
    @SerializedName("promoID")
    @Expose
    val shopID: String = "",
    @SerializedName("promoID")
    @Expose
    val promoID: String = "",
    @SerializedName("page")
    @Expose
    val page: Int = 0,
    @SerializedName("perPage")
    @Expose
    val perPage: Int = 0,
    @SerializedName("sortBy")
    @Expose
    val sortBy: String,
    @SerializedName("districtID")
    @Expose
    val districtID: String,
    @SerializedName("cityID")
    @Expose
    val cityID: String,
    @SerializedName("latitude")
    @Expose
    val latitude: String,
    @SerializedName("longitude")
    @Expose
    val longitude: String
)