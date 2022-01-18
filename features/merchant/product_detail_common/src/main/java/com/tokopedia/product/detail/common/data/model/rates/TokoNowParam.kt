package com.tokopedia.product.detail.common.data.model.rates

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoNowParam(
    @Expose
    @SerializedName("shopID")
    val shopId: String = "",

    @Expose
    @SerializedName("whID")
    val warehouseId: String = "",

    @Expose
    @SerializedName("serviceType")
    val serviceType: String = ""
)
