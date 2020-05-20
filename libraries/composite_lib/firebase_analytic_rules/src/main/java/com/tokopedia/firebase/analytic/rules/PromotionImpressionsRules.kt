package com.tokopedia.firebase.analytic.rules

import com.tokopedia.analyticparam.AnalyticParameter
import com.squareup.javapoet.ClassName

class PromotionImpressionsRules {
    companion object {
        val rules = mapOf(
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