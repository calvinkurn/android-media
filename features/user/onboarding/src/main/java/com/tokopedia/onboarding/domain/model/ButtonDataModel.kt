package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ButtonDataModel(
    @SerializedName("appLink")
    var appLink: String = "",
    @SerializedName("color")
    var color: String = "",
    @SerializedName("componentLevel")
    var componentLevel: Int = 0,
    @SerializedName("componentName")
    var componentName: String = "",
    @SerializedName("text")
    var text: String = "",
    @SerializedName("textColor")
    var textColor: String = "",
    @SerializedName("visibility")
    var visibility: Boolean = false
) : Parcelable