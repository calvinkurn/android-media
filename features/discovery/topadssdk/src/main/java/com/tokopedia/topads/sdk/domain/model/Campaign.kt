package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

private const val KEY_ORIGINAL_PRICE = "original_price"
private const val KEY_DISCOUNT_PERCENTAGE = "discount_percentage"

@Parcelize
data class Campaign(
    @SerializedName(KEY_ORIGINAL_PRICE)
    @Expose
    var originalPrice: String = "",

    @SerializedName(KEY_DISCOUNT_PERCENTAGE)
    @Expose
    var discountPercentageSet: Float = 0F
) : Parcelable {

    val discountPercentage: Int
        get() = discountPercentageSet.toInt()


}
