package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DynamicPriceData(
    @SerializedName("text_label")
    val textLabel: String = ""
) : Parcelable
