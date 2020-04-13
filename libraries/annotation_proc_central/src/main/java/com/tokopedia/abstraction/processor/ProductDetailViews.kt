package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.firebase.analytic.rules.ProductDetailViewsRules

@AnalyticEvent(true, Event.VIEW_ITEM, ProductDetailViewsRules::class)
data class ProductDetailViews(
        val items: List<Product>
)