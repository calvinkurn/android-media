package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class ImageDataModel(
    @SerializedName("animationUrl")
    var animationUrl: String = "",
    @SerializedName("componentLevel")
    var componentLevel: Int = 0,
    @SerializedName("componentName")
    var componentName: String = "",
    @SerializedName("imageUrl")
    var imageUrl: String = "",
    @SerializedName("visibility")
    var visibility: Boolean = false
) : Parcelable