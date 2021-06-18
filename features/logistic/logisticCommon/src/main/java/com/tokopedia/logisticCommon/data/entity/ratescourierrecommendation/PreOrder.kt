package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreOrder (
        @SerializedName("header")
        val header: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("display")
        val display: Boolean = false
) : Parcelable