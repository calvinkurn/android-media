package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class FeaturesData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("dynamic_price")
        val dynamicPricing: DynamicPricing = DynamicPricing()

) : Parcelable

@Parcelize
data class DynamicPricing(
        @SerializedName("text_label")
        val textLabel: String = ""
): Parcelable