package com.tokopedia.onboarding.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class NavigationDataModel(
        @SerializedName("componentName")
    var componentName: String = "",
        @SerializedName("indicators")
    var indicatorsDataModel: IndicatorsDataModel = IndicatorsDataModel(),
        @SerializedName("next")
    var nextDataModel: NextDataModel = NextDataModel(),
        @SerializedName("previous")
    var previousDataModel: PreviousDataModel = PreviousDataModel(),
        @SerializedName("skipButton")
    var skipButtonDataModel: SkipButtonDataModel = SkipButtonDataModel(),
        @SerializedName("visibility")
    var visibility: Boolean = false
) : Parcelable