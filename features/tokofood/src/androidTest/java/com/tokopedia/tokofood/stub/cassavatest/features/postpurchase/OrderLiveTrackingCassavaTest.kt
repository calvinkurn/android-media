package com.tokopedia.tokofood.stub.cassavatest.features.postpurchase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.tokofood.stub.cassavatest.base.TokoFoodPostPurchaseCassavaTest
import org.junit.Test
import org.junit.runner.RunWith

class OrderLiveTrackingCassavaTest: TokoFoodPostPurchaseCassavaTest() {

    override fun setup() {
        super.setup()
        getDriverPhoneNumberUseCaseStub.responseStub = driverPhoneNumberResponse
        getTokoFoodOrderDetailUseCaseStub.responseStub = liveTrackingResponse
        enableChatIcon()
        launchActivity()
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
//        launchActivity {
//            clickCallDriverBottomSheet(it)
//        }
        clickCallDriverBottomSheet()
    }
}
