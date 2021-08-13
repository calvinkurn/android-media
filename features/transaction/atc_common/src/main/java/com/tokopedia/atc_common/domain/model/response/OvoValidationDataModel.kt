package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OvoValidationDataModel(
        var status: Int = 0,
        var applink: String = "",
        var ovoInsufficientBalance: OvoInsufficientBalance = OvoInsufficientBalance()
): Parcelable

@Parcelize
data class OvoInsufficientBalance(
        var title: String = "",
        var description: String = "",
        var details: OvoInsufficientDetails = OvoInsufficientDetails(),
        var buttons: OvoInsufficientButton = OvoInsufficientButton()
): Parcelable

@Parcelize
data class OvoInsufficientDetails(
        var productPrice: Long = 0,
        var shippingEstimation: Int = 0,
        var ovoBalance: Int = 0,
        var topupBalance: Int = 0
): Parcelable

@Parcelize
data class OvoInsufficientButton(
        var topupButton: OvoInsufficientTopup = OvoInsufficientTopup(),
        var otherMethodButton: OvoInsufficientTopup = OvoInsufficientTopup()
): Parcelable

@Parcelize
data class OvoInsufficientTopup(
        var text: String = "",
        var applink: String = "",
        var enable: Boolean = false
): Parcelable