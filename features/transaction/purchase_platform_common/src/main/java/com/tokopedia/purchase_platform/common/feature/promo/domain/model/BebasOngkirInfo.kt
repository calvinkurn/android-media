package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class BebasOngkirInfo(
    @SerializedName("is_bo_unstack_enabled")
    val isBoUnstackEnabled: Boolean = false,
)
