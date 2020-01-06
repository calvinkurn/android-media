package com.tokopedia.purchase_platform.features.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class Data(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("status")
        val status: Boolean
)