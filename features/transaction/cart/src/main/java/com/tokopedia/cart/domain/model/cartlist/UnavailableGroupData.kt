package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnavailableGroupData(
        var title: String = "",
        var description: String = "",
        var action: List<ActionData> = emptyList(),
        var shopGroupWithErrorDataList: List<ShopGroupWithErrorData> = emptyList()
) : Parcelable