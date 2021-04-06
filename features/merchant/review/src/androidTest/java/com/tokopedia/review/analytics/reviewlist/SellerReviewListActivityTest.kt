package com.tokopedia.review.analytics.reviewlist

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.config.GlobalConfig
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.feature.inbox.common.presentation.activity.InboxReputationActivity
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class SellerReviewListActivityTest {

    companion object {
        const val CLICK_PRODUCT_PATH = "tracker/merchant/review/seller/rating_product_click_product.json"
        const val CLICK_FILTER_PATH = "tracker/merchant/review/seller/rating_product_click_filter.json"
        const val CLICK_SORT_PATH = "tracker/merchant/review/seller/rating_product_click_sort.json"
        const val CLICK_SEARCH_PATH = "tracker/merchant/review/seller/rating_product_search.json"
        const val PRODUCT_NAME_TO_SEARCH = "baju"
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    @get:Rule
    var activityRule: IntentsTestRule<InboxReputationActivity> = object : IntentsTestRule<InboxReputationActivity>(InboxReputationActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return RouteManager.getIntent(targetContext, ApplinkConst.REPUTATION)
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().toBlocking().first()
            setupGraphqlMockResponse(SellerReviewListMockResponse())
            setAppToSellerApp()
        }

        override fun afterActivityLaunched() {
            super.afterActivityLaunched()
            waitForData()
        }
    }

    @Before
    fun setup() {
        intendingIntent()
    }

    @After
    fun finish() {
        InstrumentationAuthHelper.clearUserSession()
    }

    @Test
    fun validateClickProduct() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT)
            if (!isVisibleCoachMark) {
                clickAction(com.tokopedia.coachmark.R.id.text_skip)
            }
            waitForData()
            minimizeAppBarLayout()
            scrollAndWait()
            scrollAndWait()
            scrollAndWait()
            scrollAndWait()
            Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.rvRatingProduct)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(getRatingProductItemCount() - 1, ViewActions.click()))
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, CLICK_PRODUCT_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickFilter() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT)
            if (!isVisibleCoachMark) {
                clickAction(com.tokopedia.coachmark.R.id.text_skip)
            }
            Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.review_period_filter_chips)).perform(ViewActions.click())
            waitForData()
            Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(com.tokopedia.review.R.id.listFilterRatingProduct)).atPosition(1).perform(ViewActions.click())
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, CLICK_FILTER_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickSort() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT)
            if (!isVisibleCoachMark) {
                clickAction(com.tokopedia.coachmark.R.id.text_skip)
            }
            Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.review_sort_chips)).perform(ViewActions.click())
            waitForData()
            Espresso.onData(Matchers.anything()).inAdapterView(ViewMatchers.withId(com.tokopedia.review.R.id.listSortRatingProduct)).atPosition(1).perform(ViewActions.click())
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, CLICK_SORT_PATH)
            finishTest()
        }
    }

    @Test
    fun validateClickSearch() {
        actionTest {
            val isVisibleCoachMark = CoachMark().hasShown(activityRule.activity, RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT)
            if (!isVisibleCoachMark) {
                clickAction(com.tokopedia.coachmark.R.id.text_skip)
            }
            Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.searchBarRatingProduct)).perform(ViewActions.click())
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(ViewActions.typeText(PRODUCT_NAME_TO_SEARCH))
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(ViewActions.pressImeActionButton())
        } assertTest {
            waitForData()
            performClose(activityRule)
            waitForTrackerSent()
            validate(gtmLogDBSource, targetContext, CLICK_SEARCH_PATH)
            finishTest()
        }
    }

    private fun waitForData() {
        Thread.sleep(8000L)
    }

    private fun waitForLoading() {
        Thread.sleep(3000)
    }

    private fun waitForTrackerSent() {
        Thread.sleep(5000)
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
        Thread.sleep(3000)
    }

    private fun scrollAndWait() {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.rvRatingProduct)).perform(ViewActions.swipeUp())
        waitForLoading()
    }

    private fun minimizeAppBarLayout() {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.review.R.id.appBar_layout_reviewSeller)).perform(ViewActions.swipeUp())
    }

    private fun getRatingProductItemCount(): Int  {
        return activityRule.activity.findViewById<RecyclerView>(com.tokopedia.review.R.id.rvRatingProduct).adapter?.itemCount ?: 0
    }

    private fun setAppToSellerApp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP
        GlobalConfig.DEBUG = true
    }

}