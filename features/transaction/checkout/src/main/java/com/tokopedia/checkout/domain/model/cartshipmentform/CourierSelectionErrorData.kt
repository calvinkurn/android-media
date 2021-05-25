package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CourierSelectionErrorData(
        val title: String = "",
        val description: String = ""
): Parcelable
