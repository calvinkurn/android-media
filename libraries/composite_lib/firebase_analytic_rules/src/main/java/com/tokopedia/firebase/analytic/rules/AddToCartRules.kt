package com.tokopedia.firebase.analytic.rules

import com.squareup.javapoet.ClassName
import com.tokopedia.analyticparam.AnalyticParameter
import com.tokopedia.annotation.AnalyticRules

@AnalyticRules
class AddToCartRules {
    companion object {
        val rules = mapOf(
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
                                "item_brand" to AnalyticParameter(
                                        String::class.java
                                ),
                                "item_category" to AnalyticParameter(
                                        String::class.java
                                ),
                                "item_variant" to AnalyticParameter(
                                        String::class.java),
                                "price" to AnalyticParameter(Double::class.java),
                                "quantity" to AnalyticParameter(Long::class.java),
                                "dimension45" to AnalyticParameter(String::class.java)
                        )
                ),
                "eventAction" to AnalyticParameter(
                        String::class.java
                ),
                "event" to AnalyticParameter(
                        String::class.java
                )
        )
    }
}