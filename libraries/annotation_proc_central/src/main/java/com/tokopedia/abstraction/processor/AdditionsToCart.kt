package com.tokopedia.abstraction.processor

import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.firebase.analytic.rules.AdditionsToCartRules

@AnalyticEvent(true, Event.ADD_TO_CART, AdditionsToCartRules::class)
class AdditionsToCart(
        val items: List<Product>
)