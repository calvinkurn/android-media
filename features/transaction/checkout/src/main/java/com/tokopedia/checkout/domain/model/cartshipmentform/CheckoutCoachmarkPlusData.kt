package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutCoachmarkPlusData(
    val isShown: Boolean = false,
    val title: String = "",
    val content: String = ""
) : Parcelable
