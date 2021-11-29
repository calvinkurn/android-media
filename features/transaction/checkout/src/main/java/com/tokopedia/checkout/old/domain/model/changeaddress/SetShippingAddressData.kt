package com.tokopedia.checkout.old.domain.model.changeaddress

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SetShippingAddressData(
        var isSuccess: Boolean = false,
        var messages: List<String> = emptyList(),
) : Parcelable