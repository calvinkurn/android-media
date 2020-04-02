package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.annotation.Key
import com.example.firebaseanalyticrules.rules.PromotionSelectionsRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Promotion
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(false, Event.SELECT_CONTENT, PromotionSelectionsRules::class)
data class PromotionSelections(
        @Key("promotions")
        val promotions: List<Promotion>,
        @Key(Param.CONTENT_TYPE)
        val contentType: String,
        @Key(Param.ITEM_ID)
        val itemId: String
)