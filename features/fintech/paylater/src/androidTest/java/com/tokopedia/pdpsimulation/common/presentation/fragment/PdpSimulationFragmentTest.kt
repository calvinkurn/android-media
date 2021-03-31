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
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.presentation.activity.PdpSimulationActivity
import com.tokopedia.pdpsimulation.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.*
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PdpSimulationFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(PdpSimulationActivity::class.java, false, false)

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
            addMockResponse(PAYLATER_KEY, InstrumentationMockHelper.getRawString(context, R.raw.paylaterproduct), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(APPLICATION_KEY, InstrumentationMockHelper.getRawString(context, R.raw.applicationstatusdata), MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(SIMULATION_KEY, InstrumentationMockHelper.getRawString(context, R.raw.simulationtabledata), MockModelConfig.FIND_BY_CONTAINS)
        }
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
        IdlingRegistry.getInstance().unregister(idlingResource?.countingIdlingResource)
    }

    @Test
    fun check_pay_later_click_impressions() {
        actionTest {
            testClickTabs(1)
            testClickTabs(0)
            testClickRegisterWidget()
            testClickPayLaterItem(0)
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_ANALYTICS)
            clearData()
        }
    }

    @Test
    fun check_product_info_impression() {
        actionTest {
            testClickTabs(1)
            testPayLaterProductActionImpressions()
        } assertTest {
            validate(gtmLogDBSource, context, PAY_LATER_IMPRESSION_ANALYTICS)
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
        const val PAYLATER_KEY = "paylater_getActiveProduct"
        const val APPLICATION_KEY = "getUserApplicationStatus"
        const val SIMULATION_KEY = "paylater_getSimulation"
        const val PAY_LATER_ANALYTICS = "tracker/fintech/pdpsimulation/paylater_tracking.json"
        const val PAY_LATER_IMPRESSION_ANALYTICS = "tracker/fintech/pdpsimulation/product_info_tracking.json"
    }
}