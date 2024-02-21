package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentWidget(
    val metadata: String = "",
    val enable: Boolean = false,
    val errorMessage: String = ""
) : Parcelable
