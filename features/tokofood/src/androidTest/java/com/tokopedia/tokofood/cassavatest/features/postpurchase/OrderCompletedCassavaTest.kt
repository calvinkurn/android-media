package com.tokopedia.tokofood.cassavatest.features.postpurchase

import com.tokopedia.tokofood.cassavatest.base.TokoFoodPostPurchaseCassavaTest
import org.junit.Test

class OrderCompletedCassavaTest: TokoFoodPostPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        getDriverPhoneNumberUseCaseStub.responseStub = driverPhoneNumberResponse
        getTokoFoodOrderDetailUseCaseStub.responseStub = orderCompletedResponse
        launchActivity()
    }

    @Test
    fun validateClickAddToCartCta() {
        clickAddToCartCta()
    }
}
