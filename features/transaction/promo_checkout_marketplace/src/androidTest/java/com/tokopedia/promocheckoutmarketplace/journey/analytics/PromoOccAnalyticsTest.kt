package com.tokopedia.promocheckoutmarketplace.journey.analytics

import com.tokopedia.promocheckoutmarketplace.BasePromoCheckoutMarketplaceAnalyticsTest
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.PARAM_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.PAGE_OCC
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Test

@CassavaTest
class PromoOccAnalyticsTest : BasePromoCheckoutMarketplaceAnalyticsTest() {

    override fun getAnalyticsValidatorQueryFileName(): String {
        return "tracker/transaction/promo_checkout_marketplace_one_click_checkout.json"
    }

    override fun getPageSource(): Int {
        return PAGE_OCC
    }

    override fun getStateParam(): String {
        return PARAM_CHECKOUT
    }

    override fun getCartTypeParam(): String {
        return PARAM_CHECKOUT
    }

    @Test
    fun performTest() {
        performPromoAnalyticsActions()
    }
}
