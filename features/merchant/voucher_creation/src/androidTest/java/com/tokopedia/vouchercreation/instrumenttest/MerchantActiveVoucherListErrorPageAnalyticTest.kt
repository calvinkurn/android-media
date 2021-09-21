package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.mock.MerchantActiveVoucherListErrorMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
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

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantActiveVoucherListErrorMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenErrorScreenTracker() {
        Thread.sleep(1000)
        doAnalyticDebuggerTest(VOUCHER_LIST_ACTIVE_PAGE_ERROR_OPEN)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}