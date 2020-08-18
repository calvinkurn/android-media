package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActionData(
        var id: Int = 0,
        var code: String = "",
        var message: String = ""
) : Parcelable