package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.pdpsimulation.TkpdIdlingResource
import com.tokopedia.pdpsimulation.TkpdIdlingResourceProvider
import com.tokopedia.pdpsimulation.analytics.actionTest
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.presentation.activity.PdpSimulationActivity
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
    val activityRule = ActivityTestRule(PdpSimulationActivity::class.java, false, true)

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
                SIMULATION_V2_KEY,
                InstrumentationMockHelper.getRawString(context, R.raw.simulationv2response),
                MockModelConfig.FIND_BY_CONTAINS
            )

        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }

    /**
     * Initial Impression result
     */
    @Test
    fun check_pay_later_click_impressions() {
        actionTest {
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_IMPRESSION_ANALYTICS)
            clearData()
        }
    }


    @Test
    fun check_pay_later_partner_button_click() {
        actionTest {
            clickPartnerButton()
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_PARTNER_BUTTON_CLICK)
            clearData()
        }
    }


    @Test
    fun check_pay_later_bottomsheet_button_click()
    {
        actionTest {
            clickPartnerButtonBottomSheet()
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_BOTTOM_SHEET_BUTTON_CLICK)
            clearData()
        }
    }


    @Test
    fun check_pay_later_faq_click()
    {
        actionTest {
            clickPartnerFaq()
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_FAQ_CLICK)
            clearData()
        }
    }

    @Test
    fun check_pay_later_faq_bottom_sheet_button_click()
    {
        actionTest {
            clickPartnerFaqBottomSheet()
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_FAQ_BOTTOMSHEET_BUTTON_CLICK)
            clearData()
        }
    }

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putInt(PRODUCT_PRICE, 1000000)
        bundle.putString(PARAM_PRODUCT_URL, "https://dummyurl.com")
        bundle.putString(PARAM_PRODUCT_ID, "2147828387")
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
        const val SIMULATION_V2_KEY = "paylater_getSimulationV2"
        const val PAY_LATER_IMPRESSION_ANALYTICS =
            "tracker/fintech/pdpsimulation/pay_later_open.json"
        const val PAY_LATER_PARTNER_BUTTON_CLICK =
            "tracker/fintech/pdpsimulation/partner_button_click.json"

        const val PAY_LATER_BOTTOM_SHEET_BUTTON_CLICK =
            "tracker/fintech/pdpsimulation/parter_button_bottomsheet_button_click.json"


        const val PAY_LATER_FAQ_CLICK = "tracker/fintech/pdpsimulation/patner_faq_click.json"

        const val PAY_LATER_FAQ_BOTTOMSHEET_BUTTON_CLICK =
            "tracker/fintech/pdpsimulation/parter_faq_bottomsheet_button_click.json"
    }
}