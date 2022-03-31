package com.tokopedia.activation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.activation.ActivationCheckoutMockResponseConfig
import com.tokopedia.activation.analytics.actionTest
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.pdpsimulation.TkpdIdlingResource
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.activateCheckout.presentation.activity.OptimizedCheckoutActivity
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_CODE
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ActivationCheckoutFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(OptimizedCheckoutActivity::class.java, false, false)


    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null

    @Before
    fun setUp() {
        clearData()
        login()
        setupGraphqlMockResponse(ActivationCheckoutMockResponseConfig())
        launchActivity()
        setupIdlingResource()
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }


    @Test
    fun check_simulation_test() {
        actionTest {
            waitForData()
            changeTenure()
            waitForData()
            clickOpenInstallmentDetail()
            closeBottomSheet()
            waitForData()
            proceedToOccPage()
        } assertTest {
            hasPassedAnalytics(cassavaTestRule, PAY_LATER_PARTNER_BUTTON_CLICK)
            clearData()
        }
    }


    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString(PARAM_GATEWAY_ID, "7")
        bundle.putString(PARAM_PRODUCT_ID, "2147954780")
        bundle.putString(PARAM_PRODUCT_TENURE, "3")
        bundle.putString(PARAM_GATEWAY_CODE, "GOCICIL")
        val intent = Intent(context, OptimizedCheckoutActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
    }

    private fun setupIdlingResource() {
        idlingResource = TkpdIdlingResourceProvider.provideIdlingResource("SIMULATION")
        if (idlingResource != null)
            IdlingRegistry.getInstance().register(idlingResource?.countingIdlingResource)
        else
            throw RuntimeException("No idling resource found")
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    companion object {

        const val PAY_LATER_PARTNER_BUTTON_CLICK =
            "tracker/fintech/activation_paylater.json"


    }
}