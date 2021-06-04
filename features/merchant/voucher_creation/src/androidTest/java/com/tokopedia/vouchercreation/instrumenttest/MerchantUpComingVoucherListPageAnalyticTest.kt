package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.mock.MerchantUpComingVoucherListMockModelConfig
import com.tokopedia.vouchercreation.voucherlist.view.activity.VoucherListActivity
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantUpComingVoucherListPageAnalyticTest {

    companion object {
        private const val UPCOMING_VOUCHER_LIST_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_voucher_history_list_page_open.json"
        private const val UPCOMING_VOUCHER_LIST_DUPLICATE_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_upcoming_active_voucher_list_page_duplicate_button_click.json"
        private const val UPCOMING_VOUCHER_LIST_DOWNLOAD_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_upcoming_active_voucher_list_page_download_button_click.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherListActivity> = IntentsTestRule(VoucherListActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantUpComingVoucherListMockModelConfig())
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        activityRule.launchActivity(Intent())
    }

    @After
    fun afterTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        TokopediaGraphqlInstrumentationTestHelper.deleteAllDataInDb()
    }

    @Test
    fun validateClickDuplicateButtonTracker() {
        onView(ViewMatchers.withId(R.id.rvVoucherList)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, CommonActions.clickChildViewWithId(R.id.btnMvcMore)))
        onView(ViewMatchers.withId(R.id.rvMvcBottomSheetMenu)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, ViewActions.click()))
        doAnalyticDebuggerTest(UPCOMING_VOUCHER_LIST_DUPLICATE_BUTTON_CLICK)
    }

    @Test
    fun validateClickDownloadButtonTracker() {
        onView(ViewMatchers.withId(R.id.rvVoucherList)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, CommonActions.clickChildViewWithId(R.id.btnMvcMore)))
        onView(ViewMatchers.withId(R.id.rvMvcBottomSheetMenu)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(4, ViewActions.click()))
        doAnalyticDebuggerTest(UPCOMING_VOUCHER_LIST_DOWNLOAD_BUTTON_CLICK)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }
}