package com.tokopedia.review.analytics.seller.reviewlist

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.CassavaTestFixture
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.common.Utils
import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.review.feature.reviewlist.view.fragment.RatingProductFragment
import com.tokopedia.review.stub.inbox.presentation.activity.InboxReputationActivityStub
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class SellerReviewListActivityTest: CassavaTestFixture() {

    companion object {
        const val CLICK_PRODUCT_PATH = "tracker/merchant/review/seller/rating_product_click_product.json"
        const val CLICK_FILTER_PATH = "tracker/merchant/review/seller/rating_product_click_filter.json"
        const val CLICK_SORT_PATH = "tracker/merchant/review/seller/rating_product_click_sort.json"
        const val CLICK_SEARCH_PATH = "tracker/merchant/review/seller/rating_product_search.json"
        const val PRODUCT_NAME_TO_SEARCH = "baju"
    }

    @get:Rule
    var activityRule = IntentsTestRule(
        InboxReputationActivityStub::class.java,
        false,
        false
    )

    override fun setup() {
        super.setup()
        skipCoachMark()
        setAppToSellerApp()
        mockResponses()
        val intent = InboxReputationActivityStub.getCallingIntent(context)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickProduct() {
        actionTest {
            scrollToRecyclerViewItem(R.id.rvRatingProduct, getRatingProductItemCount().dec())
            val viewInteractionRatingProduct = Espresso.onView(
                ViewMatchers.withId(R.id.rvRatingProduct)
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
            validate(cassavaTestRule, CLICK_PRODUCT_PATH)
        }
    }

    @Test
    fun validateClickFilter() {
        actionTest {
            clickAction(R.id.review_period_filter_chips)
            waitUntilBottomSheetShowingAndSettled {
                findFragmentByTag(RatingProductFragment.BOTTOM_SHEET_FILTER_TAG)
            }
            Espresso.onData(Matchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.listFilterRatingProduct)).atPosition(1)
                .perform(ViewActions.click())
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, CLICK_FILTER_PATH)
        }
    }

    @Test
    fun validateClickSort() {
        actionTest {
            clickAction(R.id.review_sort_chips)
            waitUntilBottomSheetShowingAndSettled {
                findFragmentByTag(RatingProductFragment.BOTTOM_SHEET_SORT_TAG)
            }
            Espresso.onData(Matchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.listSortRatingProduct)).atPosition(1)
                .perform(ViewActions.click())
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, CLICK_SORT_PATH)
        }
    }

    @Test
    fun validateClickSearch() {
        actionTest {
            clickAction(R.id.searchBarRatingProduct)
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield))
                .perform(ViewActions.typeText(PRODUCT_NAME_TO_SEARCH))
            Espresso.onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield))
                .perform(ViewActions.pressImeActionButton())
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, CLICK_SEARCH_PATH)
        }
    }

    private fun skipCoachMark() {
        CoachMarkPreference.setShown(
            context = context,
            tag = RatingProductFragment.TAG_COACH_MARK_RATING_PRODUCT,
            hasShown = true
        )
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun getRatingProductItemCount(): Int {
        return activityRule.activity.findViewById<RecyclerView>(R.id.rvRatingProduct).adapter?.itemCount
            ?: 0
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            ProductRatingOverallResponse::class.java,
            Utils.parseFromJson<ProductRatingOverallResponse>("mockresponse/reviewlist/get_product_rating_overall_usecase/product_rating_overall.json")
        )
        graphqlRepositoryStub.createMapResult(
            ProductReviewListResponse::class.java,
            Utils.parseFromJson<ProductReviewListResponse>("mockresponse/reviewlist/get_product_rating_overall_usecase/product_review_list.json")
        )
    }

    private fun findFragmentByTag(tag: String): BottomSheetUnify? {
        return activityRule.activity.supportFragmentManager.fragments
            .filterIsInstance<RatingProductFragment>()
            .first().fragmentManager!!.findFragmentByTag(tag) as? BottomSheetUnify
    }
}
