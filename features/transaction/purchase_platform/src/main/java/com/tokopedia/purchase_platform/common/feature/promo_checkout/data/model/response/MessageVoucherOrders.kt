package com.tokopedia.purchase_platform.common.feature.promo_checkout.data.model.response

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class MessageVoucherOrders (
        @SerializedName("state")
        var state: String = "",

        @SerializedName("color")
        var color: String = "",

        @SerializedName("text")
        var text: String = "")