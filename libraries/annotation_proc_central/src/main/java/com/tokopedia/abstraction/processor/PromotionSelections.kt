package com.tokopedia.abstraction.processor


import com.tokopedia.abstraction.processor.model.Promotion
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.Key
import com.tokopedia.firebase.analytic.rules.PromotionSelectionsRules

@AnalyticEvent(false, Event.SELECT_CONTENT, PromotionSelectionsRules::class)
data class PromotionSelections(
        @Key("promotions")
        val promotions: List<Promotion>,
        @Key(Param.CONTENT_TYPE)
        val contentType: String,
        @Key(Param.ITEM_ID)
        val itemId: String
)