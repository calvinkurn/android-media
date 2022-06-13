package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifcenter.data.annotation.UsedInNewRevamp

/**
 * @author : Steven 11/04/19
 */

@UsedInNewRevamp
data class Campaign(
    @SerializedName("active") val active: Boolean = false,
    @SerializedName("original_price_fmt") val originalPriceFormat: String = "",
    @SerializedName("discount_percentage") val discountPercentage: Int = 0,
    @SerializedName("discount_price_fmt") val discountPriceFormat: String = ""
)

@UsedInNewRevamp
data class Variant(
    @Expose @SerializedName("value") val value: String = "",
    @Expose @SerializedName("identifier") val identifier: String = "",
    @Expose @SerializedName("hex") val hex: String = ""
)