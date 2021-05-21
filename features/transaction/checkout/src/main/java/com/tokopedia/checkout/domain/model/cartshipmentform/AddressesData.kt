package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressesData(
        var active: String = "",
        var disableTabs: List<String> = emptyList(),
        var data: AddressData? = null
) : Parcelable {

    companion object {
        const val DEFAULT_ADDRESS = "default_address";
        const val TRADE_IN_ADDRESS = "trade_in_address";
    }

}