package com.tokopedia.checkout.bundle.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CourierSelectionErrorData(
        val title: String = "",
        val description: String = ""
): Parcelable
