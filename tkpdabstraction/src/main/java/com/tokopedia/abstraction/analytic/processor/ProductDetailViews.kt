package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.firebaseanalyticrules.rules.ProductDetailViewsRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(true, Event.VIEW_ITEM, ProductDetailViewsRules::class)
data class ProductDetailViews(
        val items: List<Product>
)