package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductUiModel(
        // immutable attributes
        val id: String = "",
        val name: String = "",
        val description: String = "",
        val imageURL: String = "",
        val price: Double = 0.0,
        val priceFmt: String = "",
        var subTotal: Double = 0.0,
        var subTotalFmt: String = "",
        val slashPrice: Double = 0.0,
        val slashPriceFmt: String = "",
        val isOutOfStock: Boolean = false,
        val isShopClosed: Boolean = false,
        val customListItems: List<CustomListItem> = listOf(),
        // mutable attributes
        var cartId: String = "",
        var orderQty: Int = 1,
        var orderNote: String = "",
        var isAtc: Boolean = false,
        var customOrderDetails: MutableList<CustomOrderDetail> = mutableListOf()
) : Parcelable {
    @IgnoredOnParcel
    val isSlashPriceVisible = slashPriceFmt.isNotBlank()
    @IgnoredOnParcel
    val isCustomizable = customListItems.isNotEmpty()
}