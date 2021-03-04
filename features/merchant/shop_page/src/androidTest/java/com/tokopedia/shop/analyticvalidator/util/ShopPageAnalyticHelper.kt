package com.tokopedia.shop.analyticvalidator.util

import com.tokopedia.test.application.util.InstrumentationAuthHelper

class ShopPagePrepareAnalyticHelper {
    fun mockLogin(){
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    infix fun test(action: ShopPageTestAnalyticHelper.() -> Unit) = ShopPageTestAnalyticHelper().apply(action)
}

class ShopPageTestAnalyticHelper {
    val qwe = 2
}

fun prepare(action: ShopPagePrepareAnalyticHelper.() -> Unit) = ShopPagePrepareAnalyticHelper().apply(action)