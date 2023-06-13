package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServiceBasedShipment(
    @SerializedName("is_available")
    @Expose
    val isAvailable: Boolean = false,

    @SerializedName("text_price")
    @Expose
    val textPrice: String = "",

    @SerializedName("text_eta")
    @Expose
    val textEta: String = ""
)
