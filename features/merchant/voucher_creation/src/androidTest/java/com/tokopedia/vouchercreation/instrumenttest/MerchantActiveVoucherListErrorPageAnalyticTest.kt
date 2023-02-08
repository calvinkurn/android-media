package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.mock.MerchantActiveVoucherListErrorMockModelConfig
import com.tokopedia.vouchercreation.shop.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantActiveVoucherListErrorPageAnalyticTest {

    companion object {
        private const val VOUCHER_LIST_ACTIVE_PAGE_ERROR_OPEN = "tracker/merchant/voucher_creation/mvc_active_voucher_list_page_error_state_show.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherListActivity> = IntentsTestRule(VoucherListActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun beforeTest() {
        setupGraphqlMockResponse(MerchantActiveVoucherListErrorMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenErrorScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(VOUCHER_LIST_ACTIVE_PAGE_ERROR_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                cassavaRule.validate(fileName),
                hasAllSuccess()
        )
    }
}