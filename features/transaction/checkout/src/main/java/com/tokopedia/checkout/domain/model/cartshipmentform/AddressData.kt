package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-11-07.
 */

@Parcelize
data class AddressData(
        var defaultAddress: UserAddress? = null,
        var tradeInAddress: UserAddress? = null
) : Parcelable