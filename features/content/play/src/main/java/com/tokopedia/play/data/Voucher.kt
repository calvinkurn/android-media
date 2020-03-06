package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
class Voucher (
        @SerializedName("voucherTitle")
        val title: String = "",
        @SerializedName("voucherSubtitle")
        val subtitle: String = "",
        @SerializedName("expired")
        val expired: Boolean = false,
        @SerializedName("quota")
        val quota: Int = 0,
        @SerializedName("quotaPercentage")
        val quotaPercentage: Int = 0,
        @SerializedName("thumbnailURL")
        val thumbnailUrl: String = "",
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("discountPercentageFmt")
        val discountPercentageFormatted: String = ""
)