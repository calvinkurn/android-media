package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CrossSellingConfig {
    @SerializedName("is_skipable")
    @Expose
    var isSkipAble: Boolean = false

    @SerializedName("wording")
    @Expose
    var wording: CrossSellingWording? = null
}
