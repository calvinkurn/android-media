package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ToasterActionData(
        var text: String = "",
        var showCta: Boolean = false
) : Parcelable