package com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class DeleteCartDataResponse(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("message")
    @Expose
    val message: String = ""
)
