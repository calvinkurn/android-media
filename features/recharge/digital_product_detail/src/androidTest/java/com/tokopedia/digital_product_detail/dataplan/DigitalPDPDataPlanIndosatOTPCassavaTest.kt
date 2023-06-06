package com.tokopedia.digital_product_detail.dataplan

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.digital_product_detail.dataplan.utils.DigitalPDPDataPlanIndosatOTPMockConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalPDPDataPlanIndosatOTPCassavaTest : BaseDigitalPDPDataPlanTest() {

    override fun getApplink(): String = APPLINK

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
        const val APPLINK = "tokopedia://digital/form?category_id=2&menu_id=290&template=paketdatav2"
    }
}
