package com.tokopedia.vouchercreation.instrumenttest

import android.content.Intent
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.TokopediaGraphqlInstrumentationTestHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.view.activity.VoucherDetailActivity
import com.tokopedia.vouchercreation.mock.MerchantOnGoingVoucherDetailMockModelConfig
import kotlinx.android.synthetic.main.item_mvc_detail_tips.view.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantOnGoingVoucherDetailPageAnalyticTest {

    companion object {
        private const val ONGOING_VOUCHER_DETAIL_PAGE_OPEN = "tracker/merchant/voucher_creation/mvc_ongoing_voucher_detail_page_open.json"
        private const val ONGOING_VOUCHER_DETAIL_SHARE_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_ongoing_voucher_detail_page_share_button_click.json"
        private const val ONGOING_VOUCHER_DETAIL_BOTTOM_SHEET_DOWNLOAD_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_ongoing_voucher_detail_page_bottom_sheet_download_button_click.json"
        private const val ONGOING_VOUCHER_DETAIL_BOTTOM_SHEET_SHARE_BUTTON_CLICK = "tracker/merchant/voucher_creation/mvc_ongoing_voucher_detail_page_bottom_sheet_share_button_click.json"
    }

    @get:Rule
    var activityRule: IntentsTestRule<VoucherDetailActivity> = IntentsTestRule(VoucherDetailActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun beforeTest() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(MerchantOnGoingVoucherDetailMockModelConfig())
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
        Thread.sleep(1000)
        doAnalyticDebuggerTest(ONGOING_VOUCHER_DETAIL_PAGE_OPEN)
    }

    @Test
    fun validateClickShareButtonTracker() {
        onView(withId(R.id.rvMvcVoucherDetail))
                .perform(actionOnItem<ViewHolder>(hasDescendant(allOf(withId(R.id.btnMvcFooterCta))), scrollTo()))
                .perform(actionOnItem<ViewHolder>(hasDescendant(allOf(withId(R.id.btnMvcFooterCta))), click()))
        doAnalyticDebuggerTest(ONGOING_VOUCHER_DETAIL_SHARE_BUTTON_CLICK)
    }

    @Test
    fun validateBottomSheetClickDownloadButtonTracker() {
        Thread.sleep(1000)
        onView(withId(R.id.rvMvcVoucherDetail))
                .perform(actionOnItemAtPosition<ViewHolder>(3, clickClickableSpan(R.id.tvMvcTips, "Pelajari Selengkapnya")))
        Thread.sleep(1000)
        onView(withId(R.id.scroll_view_mvc_tips_trick)).perform(swipeUp())
        onView(withId(R.id.btnMvcTipsTrickDownload)).perform(click())
        doAnalyticDebuggerTest(ONGOING_VOUCHER_DETAIL_BOTTOM_SHEET_DOWNLOAD_BUTTON_CLICK)
    }

    @Test
    fun validateBottomSheetClickShareButtonTracker() {
        Thread.sleep(1000)
        onView(withId(R.id.rvMvcVoucherDetail))
                .perform(actionOnItemAtPosition<ViewHolder>(3, clickClickableSpan(R.id.tvMvcTips, "Pelajari Selengkapnya")))
        Thread.sleep(1000)
        onView(withId(R.id.scroll_view_mvc_tips_trick)).perform(swipeUp())
        onView(withId(R.id.btnMvcTipsTrickShare)).perform(click())
        doAnalyticDebuggerTest(ONGOING_VOUCHER_DETAIL_BOTTOM_SHEET_SHARE_BUTTON_CLICK)
    }

    private fun doAnalyticDebuggerTest(fileName: String) {
        MatcherAssert.assertThat(
                getAnalyticsWithQuery(gtmLogDBSource, context, fileName),
                hasAllSuccess()
        )
    }

    private fun clickClickableSpan(id: Int, textToClick: CharSequence): ViewAction {
        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return Matchers.instanceOf(TextView::class.java)
            }

            override fun getDescription(): String {
                return "clicking on a ClickableSpan";
            }

            override fun perform(uiController: UiController, view: View) {
                val typography = view.findViewById<Typography>(id)
                val textView = typography as TextView
                val spannableString = textView.text as SpannableString
                if (spannableString.isEmpty()) {
                    // TextView is empty, nothing to do
                    throw NoMatchingViewException.Builder()
                            .includeViewHierarchy(true)
                            .withRootView(textView)
                            .build()
                }

                // Get the links inside the TextView and check if we find textToClick
                val spans = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
                if (spans.isNotEmpty()) {
                    var spanCandidate: ClickableSpan
                    for (span: ClickableSpan in spans) {
                        spanCandidate = span
                        val start = spannableString.getSpanStart(spanCandidate)
                        val end = spannableString.getSpanEnd(spanCandidate)
                        val sequence = spannableString.subSequence(start, end)
                        if (textToClick.toString() == sequence.toString()) {
                            span.onClick(textView)
                            return
                        }
                    }
                }

                // textToClick not found in TextView
                throw NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build()
            }
        }
    }
}