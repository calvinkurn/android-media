package com.tokopedia.pdpsimulation.paylater.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.pdpsimulation.PdpSimulationMockResponseConfig
import com.tokopedia.pdpsimulation.TkpdIdlingResource
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.analytics.actionTest
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.paylater.presentation.activity.PdpSimulationActivity
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PdpSimulationFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(PdpSimulationActivity::class.java, false, false)


    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        login()
        setupGraphqlMockResponse(PdpSimulationMockResponseConfig())
        launchActivity()
    }


    @Test
    fun check_simulation_test() {
        actionTest {
            waitForData()
            clickTenure()
            clickInstallmentBottomSheet()
        } assertTest {
            hasPassedAnalytics(cassavaTestRule, PAY_LATER_PARTNER_BUTTON_CLICK)
        }
    }


    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString("price", "1000000")
        bundle.putString(PARAM_PRODUCT_URL, "https://dummyurl.com")
        bundle.putString(PARAM_PRODUCT_ID, "2147828387")
        val intent = Intent(context, PdpSimulationActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
    }



    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    companion object {

        const val PAY_LATER_PARTNER_BUTTON_CLICK =
            "tracker/fintech/simulation_paylater.json"


    }
}