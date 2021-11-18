package com.tokopedia.checkout.old.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FreeShippingData(
        var eligible: Boolean = false,
        var badgeUrl: String = ""
) : Parcelable