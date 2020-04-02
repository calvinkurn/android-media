package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.annotation.defaultvalues.DefaultValueString
import com.example.firebaseanalyticrules.rules.ProductClicksRules
import com.example.firebaseanalyticrules.rules.ProductDetailViewsRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(true, Event.VIEW_ITEM_LIST, ProductDetailViewsRules::class)
data class ProductViews(
        @DefaultValueString("Search Results")
        val item_list: String,
        val items: ArrayList<Product>,
        val eventCategory: String,
        val eventAction: String,
        val event: String,
        val userId: String
)
