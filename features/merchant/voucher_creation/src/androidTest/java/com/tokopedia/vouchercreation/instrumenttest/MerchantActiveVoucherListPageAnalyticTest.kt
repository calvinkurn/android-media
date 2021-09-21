package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.mock.MerchantActiveVoucherListMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantActiveVoucherListPageAnalyticTest {

    companion object {
        private const val ACTIVE_VOUCHER_LIST_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_active_voucher_list_page_open.json"
        private const val ACTIVE_VOUCHER_LIST_SHARE_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_ongoing_active_voucher_list_page_share_button_click.json"
        private const val ACTIVE_VOUCHER_LIST_DOWNLOAD_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_ongoing_active_voucher_list_page_download_button_click.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherListActivity> = IntentsTestRule(VoucherListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantActiveVoucherListMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateOpenScreenTracker() {
        doAnalyticDebuggerTest(ACTIVE_VOUCHER_LIST_PAGE_OPEN)
    }

    @Test
    fun validateClickShareButtonTracker() {
        Thread.sleep(1000)
        onView(withId(R.id.rvVoucherList))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, CommonActions.clickChildViewWithId(R.id.btnMvcVoucherCta)))
        doAnalyticDebuggerTest(ACTIVE_VOUCHER_LIST_SHARE_BUTTON_CLICK)
    }

    @Test
    fun validateClickDownloadButtonTracker() {
        onView(withId(R.id.rvVoucherList)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, CommonActions.clickChildViewWithId(R.id.btnMvcMore)))
        onView(withId(R.id.rvMvcBottomSheetMenu)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))
        onView(withId(R.id.btnMvcDownloadVoucher)).perform(click())
        doAnalyticDebuggerTest(ACTIVE_VOUCHER_LIST_DOWNLOAD_BUTTON_CLICK)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}