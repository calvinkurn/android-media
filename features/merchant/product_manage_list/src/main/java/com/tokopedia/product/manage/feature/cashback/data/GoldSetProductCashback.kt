package com.tokopedia.product.manage.feature.cashback.data

import com.google.gson.annotations.SerializedName

data class GoldSetProductCashback(
        @SerializedName("header")
        val header: SetCashbackHeader = SetCashbackHeader()
)