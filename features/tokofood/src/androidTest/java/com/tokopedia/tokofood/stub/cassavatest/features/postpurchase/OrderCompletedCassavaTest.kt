package com.tokopedia.tokofood.stub.cassavatest.features.postpurchase

import com.tokopedia.tokofood.stub.cassavatest.base.TokoFoodPostPurchaseCassavaTest
import org.junit.Test

class OrderCompletedCassavaTest: TokoFoodPostPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        getDriverPhoneNumberUseCaseStub.responseStub = driverPhoneNumberResponse
        getTokoFoodOrderDetailUseCaseStub.responseStub = orderCompletedResponse
    }

    @Test
    fun validateClickAddToCartCta() {
        clickAddToCartCta()
    }
}
