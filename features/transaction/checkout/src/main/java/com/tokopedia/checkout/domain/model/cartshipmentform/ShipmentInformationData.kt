package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShipmentInformationData(
        var shopLocation: String = "",
        var estimation: String = "",
        var freeShipping: FreeShippingData = FreeShippingData(),
        var freeShippingExtra: FreeShippingData = FreeShippingData(),
        var preorder: PreorderData = PreorderData()
) : Parcelable