package com.tokopedia.cart.old.domain.model.cartlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnavailableGroupData(
        var title: String = "",
        var description: String = "",
        var action: List<ActionData> = emptyList(),
        var shopGroupWithErrorDataList: List<ShopGroupWithErrorData> = emptyList()
) : Parcelable