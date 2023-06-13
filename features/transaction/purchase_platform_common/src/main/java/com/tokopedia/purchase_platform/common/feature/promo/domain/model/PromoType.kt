package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class PromoType(
    @field:SerializedName("is_exclusive_shipping")
    val isExclusiveShipping: Boolean = false,
    @field:SerializedName("is_bebas_ongkir")
    val isBebasOngkir: Boolean = false
)
