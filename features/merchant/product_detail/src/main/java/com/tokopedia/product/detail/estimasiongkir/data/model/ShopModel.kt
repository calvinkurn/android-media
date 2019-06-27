package com.tokopedia.product.detail.estimasiongkir.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModel(
        @SerializedName("district_name")
        @Expose
        val districtName: String = ""
)