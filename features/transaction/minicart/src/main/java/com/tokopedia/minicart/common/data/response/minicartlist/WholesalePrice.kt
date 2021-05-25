package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class WholesalePrice(
        @SerializedName("qty_min_fmt")
        val qtyMinFmt: String = "",
        @SerializedName("qty_max_fmt")
        val qtyMaxFmt: String = "",
        @SerializedName("prd_prc_fmt")
        val prdPrcFmt: String = "",
        @SerializedName("qty_min")
        val qtyMin: Int = 0,
        @SerializedName("qty_max")
        val qtyMax: Int = 0,
        @SerializedName("prd_prc")
        val prdPrc: Long = 0
)
