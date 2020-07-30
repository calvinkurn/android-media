package com.tokopedia.product.manage.feature.cashback.data

import com.google.gson.annotations.SerializedName

data class SetCashbackResponse(
        @SerializedName("GoldSetProductCashback")
        val goldSetProductCashback: GoldSetProductCashback = GoldSetProductCashback()
)