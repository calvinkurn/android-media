package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Promotion
import com.tokopedia.analytic_constant.Event
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.firebase.analytic.rules.PromotionImpressionsRules

@AnalyticEvent(true, Event.VIEW_SEARCH_RESULTS, PromotionImpressionsRules::class)
data class PromotionImpressions(
        val promotions: List<Promotion>
)