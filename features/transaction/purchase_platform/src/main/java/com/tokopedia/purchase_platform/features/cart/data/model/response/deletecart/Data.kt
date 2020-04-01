package com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

data class Data(
        @SerializedName("success")
        val success: Int?,
        @SerializedName("message")
        val message: List<String> = emptyList()
)