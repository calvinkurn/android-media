package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class PageDataModel(
    @SerializedName("totalComponents")
    var totalComponents: Int = 0,
    @SerializedName("components")
    var componentsDataModel: ComponentsDataModel = ComponentsDataModel(),
    @SerializedName("pageName")
    var pageName: String = "",
    @SerializedName("pageNumber")
    var pageNumber: Int = 0
) : Parcelable