package com.tokopedia.cart.old.domain.model.cartlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author anggaprasetiyo on 02/03/18.
 */
@Parcelize
data class CartTickerErrorData(
        var errorInfo: String = "",
        var actionInfo: String = "",
        var errorCount: Int = 0
) : Parcelable
