package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UndoDeleteCartData(
        var isSuccess: Boolean = false,
        var message: String? = null
) : Parcelable