package com.example.googletagmanagerwithannotation.enhancedecommerce.events

import com.example.annotation.AnalyticEvent
import com.example.annotation.Key
import com.example.annotation.defaultvalues.DefaultValueInt
import com.example.annotation.defaultvalues.DefaultValueString
import com.example.firebaseanalyticrules.rules.CheckoutProcessRules
import com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param

@AnalyticEvent(false, Event.BEGIN_CHECKOUT, CheckoutProcessRules::class)
data class CheckoutProcess(
        @Key("items")
        val items: List<Product>,
        @Key(Param.CHECKOUT_STEP)
        @DefaultValueInt(1)
        val checkoutStep: Int,
        @Key(Param.CHECKOUT_OPTION)
        @DefaultValueString("Visa")
        val checkoutOption: String
)