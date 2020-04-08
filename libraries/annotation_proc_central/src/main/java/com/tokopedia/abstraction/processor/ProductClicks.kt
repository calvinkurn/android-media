package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.ProductClicksRules

@AnalyticEvent(true, Event.SELECT_CONTENT, ProductClicksRules::class)
data class ProductClicks(
        @DefaultValueString("Search Results")
        val item_list: String,
        val items: ArrayList<Product>,
        val eventCategory: String,
        val eventAction: String,
        val event: String,
        val userId: String
)
