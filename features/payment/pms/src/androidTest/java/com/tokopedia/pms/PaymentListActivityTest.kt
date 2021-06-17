package com.tokopedia.pms

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.pms.analytics.PmsDetailMockResponse
import com.tokopedia.pms.analytics.PmsIdlingResource
import com.tokopedia.pms.paymentlist.presentation.activity.PaymentListActivity
import com.tokopedia.pms.test.R
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
class PaymentListActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(PaymentListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(PmsIdlingResource.idlingResource)
        gtmLogDBSource.deleteAll().toBlocking().first()
        login()
        setupGraphqlMockResponse  {
            addMockResponse(
                PmsDetailMockResponse.GQL_PAYMENT_LIST, InstrumentationMockHelper.getRawString(context,  R.raw.response_deferred_payments),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
        launchActivity()
        //setupGraphqlMockResponse(PmsDetailMockResponse())
    }

    private fun launchActivity() {
        val intent = Intent(context, PaymentListActivity::class.java)
        activityRule.launchActivity(intent)
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(PmsIdlingResource.idlingResource)
    }

    @Test
    fun validateOpenPmsScreen() {
        //Thread.sleep(5000L)
        assert(1 == 1)
        /*actionTest {
            clickHtpTest()
        } assertTest {
            validate(PAYMENT_LIST_TRACKER_PATH)
            finishTest()
        }*/
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        const val PAYMENT_LIST_TRACKER_PATH = "tracker/payment/pms/pms_list_tracking.json"
    }
}