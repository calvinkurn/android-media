package com.tokopedia.purchase_platform.features.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 09/03/20.
 */
data class MessageGlobalPromo (
        @SerializedName("state")
        var state: String = "",

        @SerializedName("color")
        var color: String = "",

        @SerializedName("text")
        var text: String = "")