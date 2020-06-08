package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ComponentsDataModel(
    @SerializedName("button")
    var buttonDataModel: ButtonDataModel = ButtonDataModel(),
    @SerializedName("image")
    var imageDataModel: ImageDataModel = ImageDataModel(),
    @SerializedName("text")
    var textDataModel: TextDataModel = TextDataModel()
) : Parcelable