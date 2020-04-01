package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.firebaseanalyticrules.rules.AdditionsToCartRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Product
import com.tokopedia.analytic_constant.Event

@AnalyticEvent(true, Event.ADD_TO_CART, AdditionsToCartRules::class)
class AdditionsToCart(
        val items: List<Product>
)