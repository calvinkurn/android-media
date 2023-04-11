package com.tokopedia.topads.sdk.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

private const val KEY_NAME = "name"
private const val KEY_IS_AD = "is_ad"

@Parcelize
data class Template(
    @SerializedName(KEY_NAME)
    var name: String = "",

    @SerializedName(KEY_IS_AD)
    var isIsAd: Boolean = false
) : Parcelable
