package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CpmImage(
    @SerializedName("full_url")
    var fullUrl: String = "",

    @SerializedName("full_ecs")
    var fullEcs: String = "",

    @SerializedName("illustration_url")
    var ilustrationUrl: String = ""
) : ImpressHolder(), Parcelable
