package com.tokopedia.digital_product_detail.dataplan

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.digital_product_detail.dataplan.utils.DigitalPDPDataPlanIndosatOTPMockConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalPDPDataPlanIndosatOTPCassavaTest : BaseDigitalPDPDataPlanTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private fun stubIntent() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    override fun getMockModelConfig(): MockModelConfig = DigitalPDPDataPlanIndosatOTPMockConfig()

    @Before
    fun setUp() {
        stubIntent()
    }

    @Test
    fun validate_cassava() {
        interactWithCheckBalanceOTPWidget()

        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS),
            hasAllSuccess()
        )
    }

    private fun interactWithCheckBalanceOTPWidget() {
        // Check Balance OTP Widget
        Thread.sleep(2000)
        clientNumberWidget_typeNumber("0816242868")

        Thread.sleep(2000)
        clientNumberWidget_clickCheckBalanceOTPWidget()

        Thread.sleep(2000)
        checkBalanceWebView_stubIntentResult()
        checkBalanceOTPBottomSheet_clickButton()
    }

    companion object {
        const val PATH_ANALYTICS = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_otp.json"
    }
}
