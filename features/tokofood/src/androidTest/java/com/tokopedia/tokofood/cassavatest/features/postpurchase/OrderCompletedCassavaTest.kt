package com.tokopedia.tokofood.cassavatest.features.postpurchase

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
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
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(3000)
    }
}
