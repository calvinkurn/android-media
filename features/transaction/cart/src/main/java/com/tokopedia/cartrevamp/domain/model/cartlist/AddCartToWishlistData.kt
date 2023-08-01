package com.tokopedia.cartrevamp.domain.model.cartlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddCartToWishlistData(
    var status: String = "",
    var success: Int = 0,
    var message: String = ""
) : Parcelable
