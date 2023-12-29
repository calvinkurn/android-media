package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class LastApply(
    @field:SerializedName("data")
    val data: Data = Data()
)
