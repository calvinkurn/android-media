package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class IndicatorsDataModel(
    @SerializedName("DefaultColor")
    var defaultColor: String = "",
    @SerializedName("SelectedColor")
    var selectedColor: String = "",
    @SerializedName("visibility")
    var visibility: Boolean = false
) : Parcelable