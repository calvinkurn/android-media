package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OutOfServiceData(
        var id: Int = 0,
        var image: String = "",
        var title: String = "",
        var description: String = "",
        var buttons: List<ButtonData> = emptyList()
) : Parcelable