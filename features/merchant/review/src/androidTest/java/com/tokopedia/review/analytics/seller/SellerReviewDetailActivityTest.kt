package com.tokopedia.review.analytics.seller

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.core.AllOf
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
                putExtra(SellerReviewDetailFragment.CHIP_FILTER, "1 Tahun Terakhir")
                putExtra(SellerReviewDetailFragment.PRODUCT_IMAGE, "")
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.clearUserSession()
        }
    }

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse(SellerReviewDetailMockResponse())
    }

    @Test
    fun validateClickEditProduct() {
        actionTest {
            fakeLogin()
            intendingIntent()
            waitForData()
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if(!isVisibleCoachMark) {
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
    fun validateClickFilterTimeStar() {
        actionTest {
            fakeLogin()
            intendingIntent()
            waitForData()
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL)
            if(!isVisibleCoachMark) {
                clickAction(R.id.text_next)
            }
            clickFilterTime()
            clickFilterStar()
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, FILTER_TIME_STAR_PATH)
            finishTest()
        }
    }

    private fun waitForData() {
        Thread.sleep(5000L)
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
        Thread.sleep(3000)
    }

    private fun clickFilterStar() {
        val viewInteractionStar = Espresso.onView(AllOf.allOf(withId(R.id.rvRatingDetail))).check(ViewAssertions.matches(isDisplayed()))
        viewInteractionStar.perform(RecyclerViewActions.actionOnItemAtPosition<OverallRatingDetailViewHolder>(1, CommonActions.clickChildViewWithId(R.id.review_period_filter_button_detail)))
    }

    private fun clickFilterTime() {
        val viewInteractionTime = Espresso.onView(AllOf.allOf(withId(R.id.rvRatingDetail))).check(ViewAssertions.matches(isDisplayed()))
        viewInteractionTime.perform(RecyclerViewActions.actionOnItemAtPosition<RatingAndTopicDetailViewHolder>(0, CommonActions.clickChildViewWithId(R.id.rating_checkbox)))
        if (showDialogOption(TAG_FILTER_TIME)?.isVisible == true) {
            onData(anything()).inAdapterView(withId(R.id.listFilterReviewDetail)).atPosition(2).perform(click())
        }
        Thread.sleep(1000)
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

    private fun fakeLogin() {
        InstrumentationAuthHelper.loginInstrumentationTestSellerUser()
    }

    private fun waitForTrackerSent() {
        Thread.sleep(5000)
    }

    companion object {
        const val PRODUCT_ID = 669405017
        const val TAG_EDIT_PRODUCT = "Ubah Produk"
        const val TAG_FILTER_TIME = "Tampilkan periode ulasan dalam"
        const val EDIT_PRODUCT_PATH = "tracker/merchant/review/seller/review_detail_click_dot_edit_product.json"
        const val FILTER_TIME_STAR_PATH = "tracker/merchant/review/seller/review_detail_click_filter_time_star.json"
        const val REPORT_PATH = "tracker/merchant/review/seller/review_detail_click_report.json"
        const val SORT_FILTER_PATH = "tracker/merchant/review/seller/review_detail_click_sort_filter.json"
    }
}