package com.tokopedia.digital_product_detail.dataplan

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.digital_product_detail.dataplan.utils.DigitalPDPDataPlanMCCMDropdownMockConfig
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DigitalPDPDataPlanMCCMDropdownCassavaTest : BaseDigitalPDPDataPlanTest() {

    override fun getApplink(): String  = APPLINK

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private fun stubIntent() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal()))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    override fun getMockModelConfig(): MockModelConfig = DigitalPDPDataPlanMCCMDropdownMockConfig()

    @Before
    fun setUp() {
        stubIntent()
    }

    @Test
    fun validate_interact_mccm_dropdown() {
        interactWithMccmWidget()
        MatcherAssert.assertThat(
            cassavaTestRule.validate(PATH_ANALYTICS),
            hasAllSuccess()
        )
    }

    private fun interactWithMccmWidget() {
        Thread.sleep(10000)
        mccm_vertical_clickShowMore()
        mccm_vertical_clickCard_withIndex(0)
        mccm_vertical_clickCardChevron_withIndex(0)

        Thread.sleep(1000)
        productDetailBottomSheet_clickClose()
    }

    companion object {
        const val PATH_ANALYTICS = "tracker/recharge/digital_product_detail/digital_pdp_dataplan_mccm_dropdown.json"
    }
}
