package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class FeaturesData(
        @SerializedName("dynamic_price")
        val dynamicPricing: DynamicPricing = DynamicPricing()

) : Parcelable

@Parcelize
data class DynamicPricing(
        @SerializedName("text_label")
        val textLabel: String = ""
): Parcelable