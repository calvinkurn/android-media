package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 20/02/18.
 */
@Parcelize
data class UpdateCartData(
        var isSuccess: Boolean = false,
        var message: String = ""
) : Parcelable
