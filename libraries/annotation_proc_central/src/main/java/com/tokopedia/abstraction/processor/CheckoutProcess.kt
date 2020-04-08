package com.tokopedia.abstraction.processor

import com.tokopedia.abstraction.processor.model.Product
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.AnalyticEvent
import com.tokopedia.annotation.Key
import com.tokopedia.annotation.defaultvalues.DefaultValueInt
import com.tokopedia.annotation.defaultvalues.DefaultValueString
import com.tokopedia.firebase.analytic.rules.CheckoutProcessRules

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