package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class UpdateCartDataResponse(
    @SerializedName("error_message")
    val error: List<String> = emptyList(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: Data = Data()
)