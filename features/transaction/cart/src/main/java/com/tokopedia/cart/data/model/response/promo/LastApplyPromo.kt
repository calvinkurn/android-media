package com.tokopedia.cart.data.model.response.promo

import com.google.gson.annotations.SerializedName

data class LastApplyPromo(
        @SerializedName("data")
        var lastApplyPromoData: LastApplyPromoData = LastApplyPromoData(),
        @SerializedName("code")
        var code: String = ""
)