package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentWidget(
    val metadata: String = "",
    val enable: Boolean = false,
    val errorMessage: String = "",
    val chosenPayment: ChosenPayment = ChosenPayment()
) : Parcelable

@Parcelize
data class ChosenPayment(
    val gatewayCode: String = "",
    val tenureType: Int = 0,
    val optionId: String = "",
    val metadata: String = ""
): Parcelable
