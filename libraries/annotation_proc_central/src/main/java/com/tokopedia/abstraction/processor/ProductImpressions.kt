package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.firebase.analytic.rules.ProductImpressionsRules

@AnalyticEvent(true, Event.VIEW_SEARCH_RESULTS, ProductImpressionsRules::class)
class ProductImpressions(
        val item_list: String,
        val items: ArrayList<Product>
)