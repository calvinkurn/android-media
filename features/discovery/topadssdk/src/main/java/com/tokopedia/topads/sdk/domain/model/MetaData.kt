package com.tokopedia.topads.sdk.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaData(
    @SerializedName("display")
    var display: String? = null
)
