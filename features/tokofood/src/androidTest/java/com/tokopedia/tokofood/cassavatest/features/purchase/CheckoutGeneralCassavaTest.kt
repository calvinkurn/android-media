package com.tokopedia.tokofood.cassavatest.features.purchase

import com.tokopedia.tokofood.cassavatest.base.TokoFoodPurchaseCassavaTest
import org.junit.Test

class CheckoutGeneralCassavaTest: TokoFoodPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        checkoutTokoFoodUseCaseStub.responseStub = checkoutTokoFoodResponseStub
        checkoutGeneralUseCaseStub.responseStub = checkoutGeneralResponseStub
        launchActivity()
    }

    @Test
    fun clickGoToPaymentButton() {
        clickPurchaseButton()
        validateTracker(CHECKOUT_GENERAL)
        dismissPage()
    }

}
