package com.tokopedia.tokofood.cassavatest.features.purchase

import com.tokopedia.tokofood.cassavatest.base.TokoFoodPurchaseCassavaTest
import org.junit.Test

class PurchaseCassavaTest: TokoFoodPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        checkoutTokoFoodUseCaseStub.responseStub = checkoutTokoFoodResponseStub
        launchActivity()
    }

    @Test
    fun checkoutPageLoad() {
        validateTracker(LOAD_CHECKOUT_PAGE)
        dismissPage()
    }

}
