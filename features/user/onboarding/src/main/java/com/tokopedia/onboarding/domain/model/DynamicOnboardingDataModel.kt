package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class DynamicOnboardingDataModel(
    @SerializedName("navigation")
    var navigationDataModel: NavigationDataModel = NavigationDataModel(),
    @SerializedName("pages")
    var pageDataModels: MutableList<PageDataModel> = mutableListOf()
) : Parcelable