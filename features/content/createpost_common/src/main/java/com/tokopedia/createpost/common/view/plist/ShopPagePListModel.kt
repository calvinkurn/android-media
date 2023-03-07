package com.tokopedia.createpost.common.view.plist

import com.google.gson.annotations.SerializedName
import com.tokopedia.library.baseadapter.BaseItem
import java.io.Serializable


data class PrimaryImage(
    @field:SerializedName("resize300")
    val img: String
) : Serializable

data class SPrice(
    @field:SerializedName("text_idr")
    val priceIdr: String
) : Serializable

data class Campaign(
    @field:SerializedName("discounted_percentage")
    val dPrice: String,

    @field:SerializedName("discounted_price_fmt")
    val dPriceFormatted: String,

    @field:SerializedName("original_price_fmt")
    val oPriceFormatted: String,
) : Serializable