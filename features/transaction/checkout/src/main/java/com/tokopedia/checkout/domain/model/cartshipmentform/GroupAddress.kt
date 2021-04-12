package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupAddress(
        var isError: Boolean = false,
        var errorMessage: String = "",
        var userAddress: UserAddress = UserAddress(),
        var groupShop: List<GroupShop> = emptyList(),
) : Parcelable