package com.tokopedia.cart.bundle.data.model.response.promo

import com.google.gson.annotations.SerializedName

class PromoErrorDetail(
        @SerializedName("message")
        var message: String = ""
)