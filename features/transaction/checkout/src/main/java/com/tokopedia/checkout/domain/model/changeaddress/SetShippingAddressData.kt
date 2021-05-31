package com.tokopedia.checkout.domain.model.changeaddress

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SetShippingAddressData(
        var isSuccess: Boolean = false,
        var messages: List<String> = emptyList(),
) : Parcelable