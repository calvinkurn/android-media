package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName

data class PromoBebasOngkirInfo(
    @SerializedName("is_use_bebas_ongkir_only")
    val isUseBebasOngkirOnly: Boolean = false
)
