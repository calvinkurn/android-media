package com.tokopedia.digital.newcart.data.entity.response.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CrossSellingConfig {
    @SerializedName("is_skipable")
    @Expose
    var isSkipAble: Boolean = false
    @SerializedName("is_checked")
    @Expose
    var isChecked: Boolean = false
    @SerializedName("wording")
    @Expose
    var wording: CrossSellingWording? = null
    @SerializedName("wording_is_subscribed")
    @Expose
    var wordingIsSubscribed: CrossSellingWording? = null
}
