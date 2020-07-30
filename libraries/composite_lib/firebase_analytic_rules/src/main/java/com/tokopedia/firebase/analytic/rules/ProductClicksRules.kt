package com.tokopedia.firebase.analytic.rules

import com.tokopedia.analyticparam.AnalyticParameter
import com.squareup.javapoet.ClassName
import com.tokopedia.annotation.AnalyticRules

@AnalyticRules
class ProductClicksRules {
    companion object {
        val rules = mapOf(
            "item_list" to AnalyticParameter(String::class.java),
            "items" to AnalyticParameter(
                ArrayList::class.java,
                ClassName.get(
                    "com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce",
                    "Product"
                ),
                mapOf(
                    "item_id" to AnalyticParameter(
                        String::class.java
                    ),
                    "item_name" to AnalyticParameter(
                        String::class.java
                    ),
                    "item_category" to AnalyticParameter(
                        String::class.java
                    ),
                    "item_variant" to AnalyticParameter(
                        String::class.java
                    ),
                    "item_brand" to AnalyticParameter(
                        String::class.java
                    ),
                    "price" to AnalyticParameter(Double::class.java),
                    "currency" to AnalyticParameter(
                        String::class.java
                    )
                )
            )
        )
    }
}