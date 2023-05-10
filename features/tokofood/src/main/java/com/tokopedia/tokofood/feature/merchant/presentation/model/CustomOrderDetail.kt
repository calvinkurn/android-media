package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomOrderDetail(
        var cartId: String = "",
        var subTotal: Double = 0.0,
        var subTotalFmt: String = "",
        var qty: Int = 1,
        var customListItems: List<CustomListItem> = listOf()
) : Parcelable {
    @IgnoredOnParcel
    val orderNote = customListItems.firstOrNull { it.orderNote.isNotBlank() }?.orderNote ?: ""
}
