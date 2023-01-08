package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.mock.MerchantUpComingVoucherDetailMockModelConfig
import com.tokopedia.vouchercreation.shop.detail.view.activity.VoucherDetailActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantUpComingVoucherDetailPageAnalyticTest {

    companion object {
        private const val UPCOMING_VOUCHER_DETAIL_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_upcoming_voucher_detail_page_open.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherDetailActivity> = IntentsTestRule(VoucherDetailActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun beforeTest() {
        setupGraphqlMockResponse(MerchantUpComingVoucherDetailMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(UPCOMING_VOUCHER_DETAIL_PAGE_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                cassavaRule.validate(fileName),
                hasAllSuccess()
        )
    }
}