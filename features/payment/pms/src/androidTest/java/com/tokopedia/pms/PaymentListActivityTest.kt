package com.tokopedia.pms

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.pms.analytics.actionTest
import com.tokopedia.pms.paymentlist.presentation.activity.PaymentListActivity
import com.tokopedia.pms.test.R
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.not
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

    @get:Rule
    var cassavaTestRule = CassavaTestRule(false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        Intents.init()
        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )

        login()
        setupGraphqlMockResponse {
            addMockResponse(
                GQL_PAYMENT_LIST,
                InstrumentationMockHelper.getRawString(context, R.raw.response_deferred_payments),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                GQL_CANCEL_DETAIL,
                InstrumentationMockHelper.getRawString(context, R.raw.response_cancel_detail),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                GQL_BANK_DETAIL_EDIT,
                InstrumentationMockHelper.getRawString(
                    context,
                    R.raw.response_change_bank_acccount_details
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                GQL_KLIC_BCA_EDIT,
                InstrumentationMockHelper.getRawString(context, R.raw.response_change_klic_bca_id),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
        launchActivity()
    }

    @After
    fun finish() {
        Intents.release()
    }

    @Test
    fun validatePaymentListEvents() {
        actionTest {
            Thread.sleep(5000)
            testChevronClick(0)
            Thread.sleep(5000)
            clickItemOnActionBottomSheet(0)
            Thread.sleep(5000)
            clickItemOnDetailBottomSheet(0, com.tokopedia.pms.R.id.tvCancelTransaction)
            Thread.sleep(5000)
            clickDialogButton(false)
            Thread.sleep(5000)
            testCardClick(0)
            Thread.sleep(5000)
            clickItemOnDetailBottomSheet(0, com.tokopedia.pms.R.id.goToHowToPay)
            Thread.sleep(5000)
            pressBack()

            clickHtpTest(0)
            Thread.sleep(5000)
        } assertTest {
            validate(cassavaTestRule, PAYMENT_LIST_TRACKER_PATH)
            finishTest()
        }
    }

    @Test
    fun validateChangeBankAccountEvents() {
        actionTest {
            //  change klic bca id
            Thread.sleep(5000)
            testChevronClick(1)
            Thread.sleep(5000)
            clickItemOnActionBottomSheet(0)
            Thread.sleep(5000)
            actionClickString("Terapkan")

            // change bank account details
            Thread.sleep(5000)
            testChevronClick(2)
            Thread.sleep(5000)
            clickItemOnActionBottomSheet(0)
            Thread.sleep(5000)
            actionClickString("Terapkan")
            Thread.sleep(5000)

            testChevronClick(2)
            Thread.sleep(5000)
            clickItemOnActionBottomSheet(1)
            Thread.sleep(5000)
            pressBack()
        } assertTest {
            validate(cassavaTestRule, PAYMENT_EDIT_TRACKER_PATH)
            finishTest()
        }
    }

    private fun login() = InstrumentationAuthHelper.loginInstrumentationTestUser1()

    private fun finishTest() {
        activityRule.activity.finish()
    }

    private fun launchActivity() {
        val intent = Intent(context, PaymentListActivity::class.java)
        activityRule.launchActivity(intent)
    }

    companion object {
        const val PAYMENT_LIST_TRACKER_PATH = "tracker/payment/pms/pms_list_tracking.json"
        const val PAYMENT_EDIT_TRACKER_PATH = "tracker/payment/pms/pms_bank_account_tracking.json"
        const val GQL_PAYMENT_LIST = "paymentList"
        const val GQL_CANCEL_DETAIL = "cancelDetail"
        const val GQL_KLIC_BCA_EDIT = "editKlikbca"
        const val GQL_BANK_DETAIL_EDIT = "editTransfer"
    }
}
