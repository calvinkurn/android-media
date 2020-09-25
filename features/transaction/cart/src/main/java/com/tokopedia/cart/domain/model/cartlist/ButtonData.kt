package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ButtonData(
        var id: Int = 0,
        var code: String = "",
        var message: String = "",
        var color: String = ""
) : Parcelable {

    companion object {
        const val ID_START_SHOPPING = 1
        const val ID_RETRY = 2
        const val ID_HOMEPAGE = 3
        const val ID_SETTING = 4
    }

}