package com.tokopedia.tokofood.cassavatest.features.purchase

import com.tokopedia.tokofood.cassavatest.base.BaseTokoFoodCassavaTest
import org.junit.Test

class PurchaseCassavaTest: BaseTokoFoodCassavaTest() {

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
