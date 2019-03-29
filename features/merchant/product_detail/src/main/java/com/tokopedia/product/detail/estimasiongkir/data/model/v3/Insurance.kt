package com.tokopedia.product.detail.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Insurance(
        @SerializedName("insurance_price")
        @Expose
        val insurancePrice: Int = 0,

        @SerializedName("insurance_type")
        @Expose
        val insuranceType: Int = 0,

        @SerializedName("insurance_type_info")
        @Expose
        val insuranceTypeInfo: String = "",

        @SerializedName("insurance_used_default")
        @Expose
        val insuranceUsedDefault: Int = 0,

        @SerializedName("insurance_used_info")
        @Expose
        val insuranceUsedInfo: String = "",

        @SerializedName("insurance_used_type")
        @Expose
        val insuranceUsedType: Int = 0
)