package com.tokopedia.topads.debit.autotopup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutoTopUpItem(
        @SerializedName("min_credit_fmt")
        @Expose
        val minCreditFmt: String = "",

        @SerializedName("price_fmt")
        @Expose
        val priceFmt: String = "",

        @SerializedName("tkpd_product_id")
        @Expose
        val id: Int = -1
)