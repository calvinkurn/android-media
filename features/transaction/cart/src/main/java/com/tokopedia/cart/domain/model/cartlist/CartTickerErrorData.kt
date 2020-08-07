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
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is CartTickerErrorData) {
            val data = other as CartTickerErrorData?
            return errorCount == data?.errorCount && errorInfo == data.errorInfo && actionInfo == data.actionInfo
        }
        return super.equals(other)
    }
}
