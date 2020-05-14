package com.tokopedia.onboarding.domain.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class DynamicOnboardingResponseDataModel(
        @SerializedName("GetDynamicOnboarding")
        var dyanmicOnboarding: DyanmicOnboardingDataModel = DyanmicOnboardingDataModel()
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class DyanmicOnboardingDataModel(
        @SerializedName("enable")
        var isEnable: Boolean = false,
        @SerializedName("config")
        var config: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("error_message")
        var errorMessage: String = ""
) : Parcelable