package com.tokopedia.product.detail.data.model.estimasiongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressModel(
        @SerializedName("district_name")
        @Expose
        val districtName: String = "")