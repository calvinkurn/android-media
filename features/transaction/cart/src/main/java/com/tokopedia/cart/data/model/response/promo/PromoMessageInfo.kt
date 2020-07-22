package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2020-03-03.
 */
data class PromoMessageInfo (
        @SerializedName("message")
        var message: String = "",

        @SerializedName("detail")
        var detail: String = "")