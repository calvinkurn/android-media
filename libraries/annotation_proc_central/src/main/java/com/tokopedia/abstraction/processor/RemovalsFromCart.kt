package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.firebase.analytic.rules.RemovalsFromCartRules

@AnalyticEvent(true, Event.REMOVE_FROM_CART, RemovalsFromCartRules::class)
data class RemovalsFromCart(
        val items: ArrayList<Product>
)