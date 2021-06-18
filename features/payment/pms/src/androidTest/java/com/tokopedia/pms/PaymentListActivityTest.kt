package com.tokopedia.pms

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.pms.analytics.PmsDetailMockResponse
import com.tokopedia.pms.analytics.PmsIdlingResource
import com.tokopedia.pms.analytics.actionTest
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
        setupGraphqlMockResponse {
            addMockResponse(
                PmsDetailMockResponse.GQL_PAYMENT_LIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_deferred_payments),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                PmsDetailMockResponse.GQL_CANCEL_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_cancel_detail),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
        launchActivity()
    }

    @After
    fun finish() {
        gtmLogDBSource.deleteAll().toBlocking()
        IdlingRegistry.getInstance().unregister(PmsIdlingResource.idlingResource)
    }

    @Test
    fun validateCancelPaymentFlow() {
        //intendingIntent()
        actionTest {
            testChevronClick(0)
            clickItemOnActionBottomSheet(0)
            clickItemOnDetailBottomSheet(0, com.tokopedia.pms.R.id.tvCancelTransaction)
            clickDialogButton(false)
            Thread.sleep(3000)

            testCardClick(0)
            Thread.sleep(3000)
            clickItemOnDetailBottomSheet(0, com.tokopedia.pms.R.id.goToHowToPay)
            Thread.sleep(5000)
            pressBack()
            Thread.sleep(5000)
            pressBack()
            Thread.sleep(5000)

            //clickHtpTest(0)

        } assertTest {
            validate(PAYMENT_LIST_TRACKER_PATH)
            finishTest()
        }
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateCombineVaDetailEvent() {
        actionTest {
            // open Screen
            testChevronClick(3)
            clickItemOnActionBottomSheet(0)
            clickItemOnDetailBottomSheet(0, R.id.goToHowToPay)
            pressBack()
        } assertTest {
            validate(PAYMENT_LIST_TRACKER_PATH)
            finishTest()
        }
    }

    @Test
    fun validateChangeUserIdKlicBCAEvents() {
        actionTest {
            // open Screen
            testChevronClick(3)
            clickItemOnActionBottomSheet(0)

        } assertTest {
            validate(PAYMENT_LIST_TRACKER_PATH)
            finishTest()
        }
    }

    @Test
    fun validateUploadProofEvents() {

    }


    @Test
    fun validateChangeBankAccountEvents() {

    }

    @Test
    fun validateHtp() {
        val testEvent = listOf(
            mapOf(
                "userId" to ".*",
                "event" to "clickPMS",
                "eventCategory" to "pms page",
                "eventAction" to "click lihat cara bayar",
                "eventLabel" to ".*"
            )
        )
        actionTest {
            //clickHtpTest()
        } assertTest {
            validateMap(testEvent)
            finishTest()
        }
    }

    private fun login() = InstrumentationAuthHelper.loginInstrumentationTestUser1()

    private fun finishTest() {
        gtmLogDBSource.deleteAll().toBlocking()
        activityRule.activity.finish()
    }

    private fun launchActivity() {
        val intent = Intent(context, PaymentListActivity::class.java)
        activityRule.launchActivity(intent)
    }


    companion object {
        const val PAYMENT_LIST_TRACKER_PATH = "tracker/payment/pms/pms_list_tracking.json"
    }
}