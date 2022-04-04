package com.tokopedia.tokofood.common.minicartwidget.view

import com.tokopedia.tokofood.common.minicartwidget.domain.model.CartProduct

data class MiniCartUiModel(
        var cartData: MutableMap<String, CartProduct> = mutableMapOf(),
        var shopName: String = "",
        var totalPrice: Long = 0L,
        var totalProduct: Int = 0,
        var totalProductQuantity: Int = 0
)