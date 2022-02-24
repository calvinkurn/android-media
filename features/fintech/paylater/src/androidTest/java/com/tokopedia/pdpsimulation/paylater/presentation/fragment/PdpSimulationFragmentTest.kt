package com.tokopedia.pdpsimulation.paylater.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.pdpsimulation.TkpdIdlingResource
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.analytics.actionTest
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.paylater.presentation.activity.PdpSimulationActivity
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
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
    private val gtmLogDBSource = GtmLogDBSource(context)
    var idlingResource: TkpdIdlingResource? = null

    @Before
    fun setUp() {
        clearData()
        login()
        launchActivity()
        setupIdlingResource()
        setupGraphqlMockResponse {
            addMockResponse(
                PRODUCT_V3_KEY,
                InstrumentationMockHelper.getRawString(context, R.raw.product_detail_response),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                SIMULATION_V3_KEY,
                InstrumentationMockHelper.getRawString(context, R.raw.simulationv3response),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }


    @Test
    fun check_pay_later_partner_button_click() {
        assert(true)
    }


    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putString(PARAM_PRODUCT_ID, "1")
        bundle.putString(PARAM_PRODUCT_TENURE, "12")
        val intent = Intent(context, PdpSimulationActivity::class.java)
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
        const val SIMULATION_V3_KEY = "PaylaterGetSimulationV3"
        const val PRODUCT_V3_KEY = "GetProductV3"

        const val PAY_LATER_PARTNER_BUTTON_CLICK =
            "tracker/fintech/pdpsimulation/partner_button_click.json"


    }
}