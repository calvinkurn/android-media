package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddCartToWishlistData(
        var isSuccess: Boolean = false,
        var message: String = ""
) : Parcelable