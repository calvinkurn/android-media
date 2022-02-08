package com.tokopedia.loyalty.domain.entity.response.promo

import com.google.gson.annotations.SerializedName

data class PromoMenu(
    @SerializedName("data")
    var menuPromoResponse: List<MenuPromoResponse>? = null
)

data class PromoDataNew(
    @SerializedName("data")
    var promoData: List<PromoResponse>? = null
)