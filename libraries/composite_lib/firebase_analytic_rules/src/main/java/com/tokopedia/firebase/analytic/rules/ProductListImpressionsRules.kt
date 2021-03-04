package com.tokopedia.firebase.analytic.rules

import com.squareup.javapoet.ClassName
import com.tokopedia.analyticparam.AnalyticParameter
import com.tokopedia.annotation.AnalyticRules

/**
 * previously named ProductImpressionsRules
 */
@AnalyticRules
class ProductListImpressionsRules {
    companion object {
        val rules = mapOf(
                "eventAction" to AnalyticParameter(
                        String::class.java
                ),
                "event" to AnalyticParameter(
                        String::class.java
                ),
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
                                "index" to AnalyticParameter(
                                        Long::class.java
                                ),
                                "list" to AnalyticParameter(
                                        String::class.java
                                ),
                                "dimension40" to AnalyticParameter(
                                        String::class.java
                                ),
                                "dimension87" to AnalyticParameter(
                                        String::class.java
                                ),
                                "dimension88" to AnalyticParameter(
                                        String::class.java
                                )
                        )
                )
        )
    }
}