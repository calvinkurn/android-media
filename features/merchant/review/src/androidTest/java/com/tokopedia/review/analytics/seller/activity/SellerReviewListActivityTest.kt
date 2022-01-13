package com.tokopedia.review.analytics.seller.activity

import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
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
import com.tokopedia.review.common.Utils
import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.review.stub.common.di.component.BaseAppComponentStubInstance
import com.tokopedia.review.stub.common.graphql.coroutines.domain.repository.GraphqlRepositoryStub
import com.tokopedia.review.stub.inbox.presentation.activity.InboxReputationActivityStub
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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

    lateinit var graphqlRepositoryStub: GraphqlRepositoryStub

    @get:Rule
    var activityRule: ActivityTestRule<InboxReputationActivityStub> =
        IntentsTestRule(InboxReputationActivityStub::class.java, false, false)

    @Before
    fun setup() {
        setAppToSellerApp()
        getGraphqlRepositoryStub()
        mockResponses()
        gtmLogDBSource.deleteAll().toBlocking().first()
        val intent = InboxReputationActivityStub.getCallingIntent(targetContext)
        activityRule.launchActivity(intent)
    }

    @After
    fun finish() {
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
            viewInteractionRatingProduct.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    getRatingProductItemCount() - 1,
                    ViewActions.click()
                )
            )
            intendingIntent()
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
        if (!isVisibleCoachMark && coachMark.isVisible) {
            clickAction(com.tokopedia.coachmark.R.id.text_skip)
        }
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun finishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun getRatingProductFragment(): RatingProductFragment {
        return activityRule.activity.getFragmentList().filterIsInstance<RatingProductFragment>().first()
    }

    private fun getRatingProductItemCount(): Int {
        return activityRule.activity.findViewById<RecyclerView>(R.id.rvRatingProduct).adapter?.itemCount
            ?: 0
    }

    private fun setAppToSellerApp() {
        GlobalConfig.APPLICATION_TYPE = GlobalConfig.SELLER_APPLICATION
        GlobalConfig.PACKAGE_APPLICATION = GlobalConfig.PACKAGE_SELLER_APP
        GlobalConfig.DEBUG = true
    }

    private fun getGraphqlRepositoryStub() {
        graphqlRepositoryStub = BaseAppComponentStubInstance.getBaseAppComponentStub(
            targetContext.applicationContext as Application
        ).graphqlRepository() as GraphqlRepositoryStub
        graphqlRepositoryStub.clearMocks()
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            ProductRatingOverallResponse::class.java,
            Utils.parseFromJson<ProductRatingOverallResponse>("mockresponse/reviewlist/getproductratingoverallusecase/product_rating_overall.json")
        )
        graphqlRepositoryStub.createMapResult(
            ProductReviewListResponse::class.java,
            Utils.parseFromJson<ProductReviewListResponse>("mockresponse/reviewlist/getproductratingoverallusecase/product_review_list.json")
        )
    }
}