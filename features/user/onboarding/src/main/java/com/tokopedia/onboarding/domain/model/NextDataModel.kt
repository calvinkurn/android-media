package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class NextDataModel(
    @SerializedName("color")
    var color: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("visibility")
    var visibility: Boolean = false
) : Parcelable