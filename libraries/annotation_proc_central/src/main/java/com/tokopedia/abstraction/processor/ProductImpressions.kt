package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.firebaseanalyticrules.rules.ProductImpressionsRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(true, Event.VIEW_SEARCH_RESULTS, ProductImpressionsRules::class)
class ProductImpressions(
        val item_list: String,
        val items: ArrayList<Product>
)