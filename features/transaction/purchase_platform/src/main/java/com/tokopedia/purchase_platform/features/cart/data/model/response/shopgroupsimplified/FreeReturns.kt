package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 02/03/18.
 */

data class FreeReturns(
    @SerializedName("free_returns_logo")
    @Expose
    var freeReturnsLogo: String = ""
)
