package com.tokopedia.firebase.analytic.rules

import com.squareup.javapoet.ClassName
import com.tokopedia.analyticparam.AnalyticParameter
import com.tokopedia.annotation.AnalyticRules

/**
 * previously named PromotionImpressionsRules
 */
@AnalyticRules
class PromoImpressionRules {
    companion object {
        val rules = mapOf(
                "eventAction" to AnalyticParameter(
                        String::class.java
                ),
                "event" to AnalyticParameter(
                        String::class.java
                ),
                "promotions" to AnalyticParameter(
                        ArrayList::class.java,
                        ClassName.get(
                                "com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce",
                                "Promotion"
                        ),
                        mapOf(
                                "item_id" to AnalyticParameter(
                                        String::class.java
                                ),
                                "item_name" to AnalyticParameter(
                                        String::class.java
                                ),
                                "creative_name" to AnalyticParameter(
                                        String::class.java
                                ),
                                "creative_slot" to AnalyticParameter(
                                        String::class.java
                                )
                        )
                )
        )
    }
}