package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Irfan Khoirul on 23/05/18.
 */

data class WholesalePrice(
    @Expose
    @SerializedName("qty_min_fmt")
    val qtyMinFmt: String = "",
    @Expose
    @SerializedName("qty_max_fmt")
    val qtyMaxFmt: String = "",
    @Expose
    @SerializedName("prd_prc_fmt")
    val prdPrcFmt: String = "",
    @Expose
    @SerializedName("qty_min")
    val qtyMin: Int = 0,
    @Expose
    @SerializedName("qty_max")
    val qtyMax: Int = 0,
    @Expose
    @SerializedName("prd_prc")
    val prdPrc: Long = 0
)
