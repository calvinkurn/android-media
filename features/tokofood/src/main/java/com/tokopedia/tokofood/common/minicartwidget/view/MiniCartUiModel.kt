package com.tokopedia.tokofood.common.minicartwidget.view

import android.os.Parcelable
import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class MiniCartUiModel(
        var cartData: MutableMap<String, CartProduct> = mutableMapOf(),
        var shopName: String = "",
        var totalPrice: Double = 0.0,
        var totalProduct: Int = 0,
        var totalProductQuantity: Int = 0
): Parcelable