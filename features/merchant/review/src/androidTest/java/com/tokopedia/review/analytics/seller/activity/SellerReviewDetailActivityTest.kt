package com.tokopedia.review.analytics.seller.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
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
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.config.GlobalConfig
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.SellerReviewRobot
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.analytics.seller.mockresponse.SellerReviewDetailMockResponse
import com.tokopedia.review.analytics.seller.util.SellerReviewIdlingInterface
import com.tokopedia.review.analytics.seller.util.SellerReviewNetworkIdlingResource
import com.tokopedia.review.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.review.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.RatingAndTopicDetailViewHolder
import com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder.TopicViewHolder
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.review.feature.reviewreply.view.viewholder.ReviewReplyFeedbackImageViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class SellerReviewDetailActivityTest {

    companion object {
        const val PRODUCT_ID = 669405017
        const val EDIT_PRODUCT_PATH =
            "tracker/merchant/review/seller/review_detail_click_dot_edit_product.json"
        const val FILTER_TIME_PATH =
            "tracker/merchant/review/seller/review_detail_click_filter_time.json"
        const val FILTER_STAR_PATH =
            "tracker/merchant/review/seller/review_detail_click_filter_star.json"
        const val REPORT_PATH = "tracker/merchant/review/seller/review_detail_click_report.json"
        const val SORT_FILTER_PATH =
            "tracker/merchant/review/seller/review_detail_click_sort_filter.json"
        const val QUICK_FILTER_PATH =
            "tracker/merchant/review/seller/review_detail_click_quick_filter.json"
        const val CLICK_IMAGE_PATH = "tracker/merchant/review/seller/review_detail_click_image.json"
        const val QUALITY_TOPIC = "kualitas"
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: ActivityTestRule<SellerReviewDetailActivity> =
        IntentsTestRule(SellerReviewDetailActivity::class.java, false, false)

    private val idlingResource by lazy {
        SellerReviewNetworkIdlingResource(object : SellerReviewIdlingInterface {
            override fun getName(): String = "SellerReviewDetailIdle"

            override fun idleState(): Boolean {
                val fragment =
                    activityRule.activity.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as SellerReviewDetailFragment
                if (fragment.view?.findViewById<RecyclerView>(R.id.rvRatingDetail) == null) {
                    throw RuntimeException("Recyclerview not found")
                }

                return fragment.view?.findViewById<RecyclerView>(R.id.rvRatingDetail)?.visibility == View.VISIBLE
            }
        })
    }


    @Before
    fun setup() {
        setAppToSellerApp()
        setupGraphqlMockResponse(SellerReviewDetailMockResponse())
        gtmLogDBSource.deleteAll().toBlocking().first()
        setUpTimeoutIdlingResource()
        val intent = Intent(targetContext, SellerReviewDetailActivity::class.java).apply {
            putExtra(SellerReviewDetailFragment.PRODUCT_ID, PRODUCT_ID)
            putExtra(SellerReviewDetailFragment.PRODUCT_IMAGE, "")
        }
        activityRule.launchActivity(intent)
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        finishTest()
    }

    @Test
    fun validateClickEditProduct() {
        actionTest {
            skipCoachMark()
            clickAction(R.id.menu_option_product_detail)
            intendingIntent()
            clickEditProduct()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, EDIT_PRODUCT_PATH)
        }
    }

    @Test
    fun validateClickFilterTime() {
        actionTest {
            skipCoachMark()
            clickFilterTime()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, FILTER_TIME_PATH)
        }
    }

    @Test
    fun validateClickFilterStar() {
        actionTest {
            skipCoachMark()
            clickFilterStar()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, FILTER_STAR_PATH)
        }
    }

    @Test
    fun validateClickReport() {
        actionTest {
            skipCoachMark()
            clickReportDetail()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, REPORT_PATH)
        }
    }

    @Test
    fun validateClickSortFilter() {
        actionTest {
            skipCoachMark()
            clickSortFilter()
            waitForResume()
            clickAction(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, SORT_FILTER_PATH)
        }
    }

    @Test
    fun validateClickQuickFilter() {
        actionTest {
            skipCoachMark()
            clickQuickFilter()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, QUICK_FILTER_PATH)
        }
    }

    @Test
    fun validateClickImage() {
        actionTest {
            skipCoachMark()
            clickImage()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, CLICK_IMAGE_PATH)
        }
    }

    private fun SellerReviewRobot.skipCoachMark() {
        val isVisibleCoachMark = CoachMark().hasShown(
            activityRule.activity,
            SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL
        )
        if (!isVisibleCoachMark) {
            clickAction(com.tokopedia.coachmark.R.id.text_next)
        }
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun clickReportDetail() {
        onView(withId(R.id.rvRatingDetail)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                scrollTo()
            )
        )
        val viewInteractionReport =
            onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionReport.perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductFeedbackDetailViewHolder>(
                3,
                CommonActions.clickChildViewWithId(R.id.ivOptionReviewFeedback)
            )
        )
        intendingIntent()
        onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(1)
            .perform(click())
    }

    private fun clickSortFilter() {
        waitForResume()
        onView(withId(R.id.rvRatingDetail)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                getPositionViewHolderByClass<FeedbackUiModel>(),
                scrollTo()
            )
        )
        val viewInteractionSortFilter =
            onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionSortFilter.perform(
            RecyclerViewActions.actionOnItemAtPosition<TopicViewHolder>(
                getPositionViewHolderByClass<TopicUiModel>(),
                CommonActions.clickChildViewWithId(com.tokopedia.sortfilter.R.id.sort_filter_prefix)
            )
        )
        waitForResume()
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(
            ViewActions.swipeUp()
        )
        waitForResume()
        onView(withId(R.id.rvSortFilter)).check(matches(isDisplayed())).perform(
            RecyclerViewActions.actionOnItemAtPosition<SortListAdapter.SortListViewHolder>(
                2,
                click()
            )
        )
        waitForResume()
        onView(withId(R.id.rvTopicFilter)).perform(
            RecyclerViewActions.actionOnItemAtPosition<TopicListAdapter.TopicListViewHolder>(
                1,
                click()
            )
        )
    }

    private fun waitForResume() {
        Thread.sleep(1000)
    }

    private fun clickQuickFilter() {
        onView(withId(R.id.rvRatingDetail)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                scrollTo()
            )
        )
        onView(
            Matchers.allOf(
                isDescendantOfA(withId(R.id.sort_filter_items_wrapper)), withText(
                    startsWith(
                        QUALITY_TOPIC
                    )
                )
            )
        ).perform(click())
    }

    private fun clickImage() {
        onView(withId(R.id.rvRatingDetail)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                scrollTo()
            )
        )
        onView(
            firstView(
                Matchers.allOf(
                    isDescendantOfA(withId(R.id.rvRatingDetail)),
                    withId(R.id.rvItemAttachmentFeedback),
                    isDisplayed()
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<ReviewReplyFeedbackImageViewHolder>(
                0,
                click()
            )
        )
    }

    private fun clickFilterStar() {
        val viewInteractionStar = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionStar.perform(
            RecyclerViewActions.actionOnItemAtPosition<RatingAndTopicDetailViewHolder>(
                getPositionViewHolderByClass<ProductReviewFilterUiModel>(),
                CommonActions.clickChildViewWithId(R.id.rating_checkbox)
            )
        )
    }

    private fun clickFilterTime() {
        val viewInteractionTime = onView(withId(R.id.rvRatingDetail)).check(matches(isDisplayed()))
        viewInteractionTime.perform(
            RecyclerViewActions.actionOnItemAtPosition<OverallRatingDetailViewHolder>(
                0,
                CommonActions.clickChildViewWithId(R.id.review_period_filter_button_detail)
            )
        )
        intendingIntent()
        onData(anything()).inAdapterView(withId(R.id.listFilterReviewDetail)).atPosition(1)
            .perform(click())
    }

    private inline fun <reified T : Visitable<*>> getPositionViewHolderByClass(): Int {
        val fragment =
            activityRule.activity.supportFragmentManager.findFragmentByTag("TAG_FRAGMENT") as SellerReviewDetailFragment
        return fragment.adapter?.list?.indexOfFirst {
            it is T
        } ?: 0
    }

    private fun clickEditProduct() {
        onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(0)
            .perform(click())
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setUpTimeoutIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    private fun setAppToSellerApp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP
    }
}