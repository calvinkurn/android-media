package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ConfigDataModel(
    @SerializedName("user-ob-navigation")
    var navigationDataModel: NavigationDataModel = NavigationDataModel(),
    @SerializedName("user-ob-pages")
    var pageDataModels: MutableList<PageDataModel> = mutableListOf()
) : Parcelable