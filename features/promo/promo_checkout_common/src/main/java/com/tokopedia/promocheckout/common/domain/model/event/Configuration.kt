package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Configuration(
        @SerializedName("conv_fee")
        @Expose
        val convFee: Int = 0,
        @SerializedName("price")
        @Expose
        val price: Int = 0,
        @SerializedName("sub_config")
        @Expose
        val subConfig: SubConfig = SubConfig()
)