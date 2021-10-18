package com.tokopedia.checkout.old.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FreeShippingData(
        var eligible: Boolean = false,
        var badgeUrl: String = ""
) : Parcelable