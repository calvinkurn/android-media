package com.tokopedia.promocheckoutmarketplace.journey.analytics

import com.tokopedia.promocheckoutmarketplace.BasePromoCheckoutMarketplaceAnalyticsTest
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.PARAM_DEFAULT
import com.tokopedia.purchase_platform.common.constant.PAGE_CART
import org.junit.Test

class PromoCartAnalyticsTest : BasePromoCheckoutMarketplaceAnalyticsTest() {

    override fun getAnalyticsValidatorQueryFileName(): String {
        return "tracker/transaction/promo_checkout_marketplace_cart.json"
    }

    override fun getPageSource(): Int {
        return PAGE_CART
    }

    override fun getStateParam(): String {
        return PARAM_DEFAULT
    }

    override fun getCartTypeParam(): String {
        return PARAM_DEFAULT
    }

    @Test
    fun performTest() {
        performPromoAnalyticsActions()
    }

}