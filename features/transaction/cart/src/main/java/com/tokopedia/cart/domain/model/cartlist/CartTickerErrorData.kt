package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author anggaprasetiyo on 02/03/18.
 */
@Parcelize
data class CartTickerErrorData(
        var errorInfo: String? = null,
        var actionInfo: String? = null,
        var errorCount: Int = 0
) : Parcelable
