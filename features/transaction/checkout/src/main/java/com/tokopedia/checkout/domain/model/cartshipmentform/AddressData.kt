package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressData(
        var defaultAddress: UserAddress? = null,
        var tradeInAddress: UserAddress? = null
) : Parcelable