package com.tokopedia.entertainment.pdp.data.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OtherCharge(
        @SerializedName("conv_fee")
        @Expose
        val convFee: Int = 0
)