package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.firebaseanalyticrules.rules.PromotionImpressionsRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Promotion
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(true, Event.VIEW_SEARCH_RESULTS, PromotionImpressionsRules::class)
data class PromotionImpressions(
        val promotions: List<Promotion>
)