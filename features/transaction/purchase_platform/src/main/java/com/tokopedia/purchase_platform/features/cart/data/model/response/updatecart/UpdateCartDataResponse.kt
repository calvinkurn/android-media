package com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class UpdateCartDataResponse(
    @SerializedName("error")
    @Expose
    val error: String = "",
    @SerializedName("status")
    @Expose
    val isStatus: Boolean = false,
    @SerializedName("goto")
    @Expose
    val goto: Int = 0
)