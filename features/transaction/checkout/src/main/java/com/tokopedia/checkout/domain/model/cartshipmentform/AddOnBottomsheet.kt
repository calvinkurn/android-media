package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnBottomsheet(
        var ticker: AddOnTicker = AddOnTicker(),
        var headerTitle: String = "",
        var description: String = "",
        var products: List<AddOnProductsItem> = emptyList()
): Parcelable
