package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse

data class OrderData(
        var cart: OrderCart = OrderCart(),
        var profileIndex: String = "",
        var preference: ProfileResponse = ProfileResponse(),
        var promo: OrderPromo = OrderPromo()
)