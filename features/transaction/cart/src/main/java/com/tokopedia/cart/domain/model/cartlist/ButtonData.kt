package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonData(
        var id: String = "",
        var code: String = "",
        var message: String = "",
        var color: String = ""
) : Parcelable