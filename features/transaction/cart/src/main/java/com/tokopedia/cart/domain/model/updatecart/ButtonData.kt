package com.tokopedia.cart.domain.model.updatecart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonData(
        var text: String = "",
        var link: String = "",
        var action: String = "",
        var color: String = ""
) : Parcelable