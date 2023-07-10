package com.tokopedia.tokofood.cassavatest.features.purchase

import com.tokopedia.tokofood.cassavatest.base.BaseTokoFoodCassavaTest
import org.junit.Test

class CheckoutGeneralCassavaTest: BaseTokoFoodCassavaTest() {

    override fun setup() {
        super.setup()
        checkoutTokoFoodUseCaseStub.responseStub = checkoutTokoFoodResponseStub
        checkoutGeneralUseCaseStub.responseStub = checkoutGeneralResponseStub
        launchActivity()
    }

    @Test
    fun clickGoToPaymentButton() {
        clickPurchaseButton()
        validateTracker(CHECKOUT_GENERAL_PAGE)
        dismissPage()
    }

}
