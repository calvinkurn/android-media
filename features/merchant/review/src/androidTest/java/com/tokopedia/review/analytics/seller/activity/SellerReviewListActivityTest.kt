package com.tokopedia.review.analytics.seller.activity

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingPolicies
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.config.GlobalConfig
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.SellerReviewRobot
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.analytics.seller.mockresponse.SellerReviewListMockResponse
import com.tokopedia.review.analytics.seller.util.SellerReviewIdlingInterface
import com.tokopedia.review.analytics.seller.util.SellerReviewNetworkIdlingResource
import com.tokopedia.review.feature.inbox.presentation.InboxReputationActivity
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class SellerReviewListActivityTest {

    companion object {
        const val CLICK_PRODUCT_PATH =
            "tracker/merchant/review/seller/rating_product_click_product.json"
        const val CLICK_FILTER_PATH =
            "tracker/merchant/review/seller/rating_product_click_filter.json"
        const val CLICK_SORT_PATH = "tracker/merchant/review/seller/rating_product_click_sort.json"
        const val CLICK_SEARCH_PATH = "tracker/merchant/review/seller/rating_product_search.json"
        const val PRODUCT_NAME_TO_SEARCH = "baju"
    }

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(targetContext)

    private val idlingResource by lazy {
        SellerReviewNetworkIdlingResource(object : SellerReviewIdlingInterface {
            override fun getName(): String = "SellerReviewListIdle"

            override fun idleState(): Boolean {
                val fragment = getRatingProductFragment()
                if (fragment.view?.findViewById<RecyclerView>(R.id.rvRatingProduct) == null) {
                    throw RuntimeException("Recyclerview not found")
                }

                return fragment.view?.findViewById<RecyclerView>(R.id.rvRatingProduct)?.visibility == View.VISIBLE
            }
        })
    }

    @get:Rule
    var activityRule: ActivityTestRule<InboxReputationActivity> =
        IntentsTestRule(InboxReputationActivity::class.java, false, false)

    @Before
    fun setup() {
        setAppToSellerApp()
        setupGraphqlMockResponse(SellerReviewListMockResponse())
        gtmLogDBSource.deleteAll().toBlocking().first()
        setUpTimeoutIdlingResource()
        val intent = InboxReputationActivity.getCallingIntent(targetContext)
        activityRule.launchActivity(intent)
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(idlingResource)
        finishTest()
    }

    @Test
    fun validateClickProduct() {
        actionTest {
            skipCoachMark()
            val viewInteractionRatingProduct =
                Espresso.onView(ViewMatchers.withId(R.id.rvRatingProduct))
            viewInteractionRatingProduct.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    getRatingProductItemCount() - 1,
                    ViewActions.scrollTo()
                )
            )
            viewInteractionRatingProduct.perform(
                ViewActions.swipeDown()
            )
            viewInteractionRatingProduct.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    getRatingProductItemCount() - 1,
                    ViewActions.scrollTo()
                )
            )
            waitForResume()
            viewInteractionRatingProduct.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    getRatingProductItemCount() - 1,
                    ViewActions.click()
                )
            )
            intendingIntent()
            waitForResume()
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, CLICK_PRODUCT_PATH)
        }
    }

    @Test
    fun validateClickFilter() {
        actionTest {
            skipCoachMark()
            Espresso.onView(ViewMatchers.withId(R.id.review_period_filter_chips))
                .perform(ViewActions.click())
            Espresso.onData(Matchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.listFilterRatingProduct)).atPosition(1)
                .perform(ViewActions.click())
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, CLICK_FILTER_PATH)
        }
    }

    @Test
    fun validateClickSort() {
        actionTest {
            skipCoachMark()
            Espresso.onView(ViewMatchers.withId(R.id.review_sort_chips))
                .perform(ViewActions.click())
            Espresso.onData(Matchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.listSortRatingProduct)).atPosition(1)
                .perform(ViewActions.click())
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, CLICK_SORT_PATH)
        }
    }

    @Test
    fun validateClickSearch() {
        actionTest {
            skipCoachMark()
            Espresso.onView(ViewMatchers.withId(R.id.searchBarRatingProduct))
                .perform(ViewActions.click())
            Espresso.onIdle()
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield))
                .perform(ViewActions.typeText(PRODUCT_NAME_TO_SEARCH))
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield))
                .perform(ViewActions.pressImeActionButton())
        } assertTest {
            performClose(activityRule)
            validate(gtmLogDBSource, targetContext, CLICK_SEARCH_PATH)
        }
    }

    private fun SellerReviewRobot.skipCoachMark() {
        val coachMark = getRatingProductFragment().coachMark
        val isVisibleCoachMark = coachMark.hasShown(
            activityRule.activity,
            RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT
        )
        waitForResume()
        if (!isVisibleCoachMark && coachMark.isVisible) {
            clickAction(com.tokopedia.coachmark.R.id.text_skip)
        }
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun finishTest() {
        Thread.sleep(2000)
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun scrollAndWait() {
        waitForResume()
        Espresso.onView(ViewMatchers.withId(R.id.rvRatingProduct)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                getRatingProductItemCount() - 1,
                ViewActions.scrollTo()
            )
        )
    }

    private fun getRatingProductFragment(): RatingProductFragment {
        return activityRule.activity.fragmentList[0] as RatingProductFragment
    }

    private fun waitForResume() {
        Thread.sleep(1000)
    }

    private fun getRatingProductItemCount(): Int {
        return activityRule.activity.findViewById<RecyclerView>(R.id.rvRatingProduct).adapter?.itemCount
            ?: 0
    }

    private fun setUpTimeoutIdlingResource() {
        IdlingPolicies.setMasterPolicyTimeout(5, TimeUnit.MINUTES)
        IdlingPolicies.setIdlingResourceTimeout(5, TimeUnit.MINUTES)
        IdlingRegistry.getInstance().register(idlingResource)
    }

    private fun setAppToSellerApp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP
        GlobalConfig.DEBUG = true
    }

}