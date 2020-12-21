package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 20/02/18.
 */

@Parcelize
data class DeleteCartData(
        var isSuccess: Boolean = false,
        var message: String? = null
) : Parcelable