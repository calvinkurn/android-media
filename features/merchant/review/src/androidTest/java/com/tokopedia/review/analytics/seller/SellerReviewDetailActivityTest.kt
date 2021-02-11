package com.tokopedia.review.analytics.seller

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.review.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.TopicViewHolder
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.hamcrest.CoreMatchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SellerReviewDetailActivityTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<SellerReviewDetailActivity> = object : IntentsTestRule<SellerReviewDetailActivity>(SellerReviewDetailActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return Intent(targetContext, SellerReviewDetailActivity::class.java).apply {
                putExtra(SellerReviewDetailFragment.PRODUCT_ID, PRODUCT_ID)
                putExtra(SellerReviewDetailFragment.CHIP_FILTER, TIME_ONE_YEAR)
                putExtra(SellerReviewDetailFragment.PRODUCT_IMAGE, "")
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            userLogin()
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(SellerReviewDetailMockResponse())
        intendingIntent()
    }

    @After
    fun finish() {
        InstrumentationAuthHelper.clearUserSession()
    }

    @Test
    fun validateClickEditProduct() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if (!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickAction(R.id.menu_option_product_detail)
            intendingIntent()
            clickEditProduct()
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, EDIT_PRODUCT_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickFilterTime() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if (!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickFilterTime()
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, FILTER_TIME_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickFilterStar() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if (!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickFilterStar()
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, FILTER_STAR_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickReport() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if (!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickReportDetail()
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, REPORT_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickSortFilter() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if (!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickSortFilter()
            clickAction(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)
        } assertTest {
            waitForTrackerSent()
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, SORT_FILTER_PATH)
            finishTest()
        }
    }

    private fun waitForData() {
        Thread.sleep(8000L)
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
        Thread.sleep(3000)
    }

    private fun clickReportDetail() {
        onView(withId(R.id.rvRatingDetail)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(4, scrollTo()))
        Thread.sleep(1000L)
        val viewInteractionReport = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionReport.perform(RecyclerViewActions.actionOnItemAtPosition<ProductFeedbackDetailViewHolder>(3, CommonActions.clickChildViewWithId(R.id.ivOptionReviewFeedback)))
        intendingIntent()
        if(showDialogOption(TAG_OPTION_FEEDBACK)?.isVisible == true) {
            onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(1).perform(click())
        }
    }

    private fun clickSortFilter() {
        onView(withId(R.id.rvRatingDetail)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(4, scrollTo()))
        Thread.sleep(1000L)
        val viewInteractionSortFilter = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionSortFilter.perform(RecyclerViewActions.actionOnItemAtPosition<TopicViewHolder>(2, CommonActions.clickChildViewWithId(R.id.sort_filter_prefix)))
        intendingIntent()
        if(showDialogOption(TAG_SORT_FILTER)?.isVisible == true) {
            onView(withId(R.id.rvSortFilter)).check(matches(isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<SortListAdapter.SortListViewHolder>(1, click()))
        }
    }

    private fun clickFilterStar() {
        val viewInteractionStar = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionStar.perform(RecyclerViewActions.actionOnItemAtPosition<RatingAndTopicDetailViewHolder>(1, CommonActions.clickChildViewWithId(R.id.rating_checkbox)))
    }

    private fun clickFilterTime() {
        val viewInteractionTime = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionTime.perform(RecyclerViewActions.actionOnItemAtPosition<OverallRatingDetailViewHolder>(0, CommonActions.clickChildViewWithId(R.id.review_period_filter_button_detail)))
        intendingIntent()
        if (showDialogOption(TAG_FILTER_TIME)?.isVisible == true) {
            onData(anything()).inAdapterView(withId(R.id.listFilterReviewDetail)).atPosition(1).perform(click())
        }
    }

    private fun clickEditProduct() {
        if (showDialogOption(TAG_EDIT_PRODUCT)?.isVisible == true) {
            onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(0).perform(click())
        }
    }

    private fun showDialogOption(tag: String): BottomSheetUnify? {
        return activityRule.activity.supportFragmentManager.findFragmentByTag(tag) as? BottomSheetUnify
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun userLogin() {
        val activityRuleLogin = ActivityTestRule(LoginActivity::class.java, false, false)
        val bundle = Bundle()
        bundle.putBoolean(LoginEmailPhoneFragment.IS_AUTO_FILL, true)
        bundle.putString(LoginEmailPhoneFragment.AUTO_FILL_EMAIL, EMAIL_LOGIN)
        val intent = Intent(targetContext, LoginActivity::class.java)
        intent.putExtras(bundle)
        activityRuleLogin.launchActivity(intent)

        onView(withId(com.tokopedia.loginregister.R.id.register_btn)).perform(click())

        waitForLoading()

        onView(withId(com.tokopedia.loginregister.R.id.wrapper_password)).perform(click())

        onView(withId(com.tokopedia.loginregister.R.id.password))
                .perform(ViewActions.typeText(PASSWORD_LOGIN))

        onView(withId(com.tokopedia.loginregister.R.id.password))
                .check(matches(withText(PASSWORD_LOGIN)))

        onView(withId(com.tokopedia.loginregister.R.id.register_btn))
                .perform(click())
    }

    private fun waitForLoading() {
        Thread.sleep(3000)
    }

    private fun waitForTrackerSent() {
        Thread.sleep(5000)
    }

    companion object {
        const val PRODUCT_ID = 669405017
        const val TAG_EDIT_PRODUCT = "Ubah Produk"
        const val TAG_FILTER_TIME = "Tampilkan periode ulasan dalam"
        const val TAG_OPTION_FEEDBACK = "Menu"
        const val TAG_SORT_FILTER = "Filter"
        const val TIME_ONE_YEAR = "1 Tahun Terakhir"
        const val EMAIL_LOGIN = "transaction.team+seller01@tokopedia.com"
        const val PASSWORD_LOGIN = "123toped789"
        const val EDIT_PRODUCT_PATH = "tracker/merchant/review/seller/review_detail_click_dot_edit_product.json"
        const val FILTER_TIME_PATH = "tracker/merchant/review/seller/review_detail_click_filter_time.json"
        const val FILTER_STAR_PATH = "tracker/merchant/review/seller/review_detail_click_filter_star.json"
        const val REPORT_PATH = "tracker/merchant/review/seller/review_detail_click_report.json"
        const val SORT_FILTER_PATH = "tracker/merchant/review/seller/review_detail_click_sort_filter.json"
    }
}