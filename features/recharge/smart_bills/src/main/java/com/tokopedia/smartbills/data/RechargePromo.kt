package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Promo(
        @SerializedName("percentage")
        @Expose
        val percentage: Int = 0,
        @SerializedName("slashAmout")
        @Expose
        val slashAmout: Int = 0,
        @SerializedName("slashAmountText")
        @Expose
        val slashAmountText: String = "",
)