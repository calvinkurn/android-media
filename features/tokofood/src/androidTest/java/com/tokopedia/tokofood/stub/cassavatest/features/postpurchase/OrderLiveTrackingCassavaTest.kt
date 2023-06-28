package com.tokopedia.tokofood.stub.cassavatest.features.postpurchase

import com.tokopedia.tokofood.stub.cassavatest.base.TokoFoodPostPurchaseCassavaTest
import org.junit.Test

class OrderLiveTrackingCassavaTest: TokoFoodPostPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        getDriverPhoneNumberUseCaseStub.responseStub = driverPhoneNumberResponse
        getTokoFoodOrderDetailUseCaseStub.responseStub = liveTrackingResponse
    }

    @Test
    fun validateClickHelpCta() {
        clickHelpCta()
    }

    @Test
    fun validateClickCallIcon() {
        clickCallIcon()
    }

    @Test
    fun validateClickChatIcon() {
        clickChatIcon()
    }

    @Test
    fun validateClickCallDriverBottomSheet() {
        clickCallDriverBottomSheet()
    }
}
