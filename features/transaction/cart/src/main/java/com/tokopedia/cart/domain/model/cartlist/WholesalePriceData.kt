package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Irfan Khoirul on 23/05/18.
 */

@Parcelize
data class WholesalePriceData(
        var qtyMinFmt: String? = null,
        var qtyMaxFmt: String? = null,
        var prdPrcFmt: String? = null,
        var qtyMin: Int = 0,
        var qtyMax: Int = 0,
        var prdPrc: Long = 0
) : Parcelable
