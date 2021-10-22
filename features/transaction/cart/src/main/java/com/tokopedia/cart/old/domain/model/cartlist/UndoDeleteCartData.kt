package com.tokopedia.cart.old.domain.model.cartlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UndoDeleteCartData(
        var isSuccess: Boolean = false,
        var message: String? = null
) : Parcelable