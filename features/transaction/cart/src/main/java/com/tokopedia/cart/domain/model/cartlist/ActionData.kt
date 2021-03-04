package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ActionData(
        var id: Int = 0,
        var code: String = "",
        var message: String = ""
) : Parcelable {

    companion object {
        const val ACTION_WISHLIST = 1
        const val ACTION_DELETE = 2
        const val ACTION_NOTES = 3
        const val ACTION_VALIDATENOTES = 4
        const val ACTION_SIMILARPRODUCT = 5
        const val ACTION_CHECKOUTBROWSER = 6
        const val ACTION_SHOWLESS = 7
        const val ACTION_SHOWMORE = 8
        const val ACTION_VERIFICATION = 9
        const val ACTION_WISHLISTED = 10
        const val ACTION_FOLLOWSHOP = 11
    }

}