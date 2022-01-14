package com.tokopedia.review.analytics.seller.reviewdetail

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.coachmark.CoachMarkPreference
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.CassavaTestFixture
import com.tokopedia.review.analytics.common.SellerReviewRobot
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.common.Utils
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackFilterResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.view.bottomsheet.BaseTopicsBottomSheet
import com.tokopedia.review.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.ProductReviewFilterUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.review.feature.reviewreply.view.viewholder.ReviewReplyFeedbackImageViewHolder
import com.tokopedia.review.stub.reviewdetail.view.activity.SellerReviewDetailActivityStub
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.startsWith
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class SellerReviewDetailActivityTest: CassavaTestFixture() {

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

    @get:Rule
    var activityRule: ActivityTestRule<SellerReviewDetailActivityStub> =
        IntentsTestRule(SellerReviewDetailActivityStub::class.java, false, false)

    override fun setup() {
        super.setup()
        skipCoachMark()
        setAppToSellerApp()
        mockResponses()
        val intent = Intent(context, SellerReviewDetailActivityStub::class.java).apply {
            putExtra(SellerReviewDetailFragment.PRODUCT_ID, PRODUCT_ID)
            putExtra(SellerReviewDetailFragment.PRODUCT_IMAGE, "")
        }
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateClickEditProduct() {
        actionTest {
            clickAction(R.id.menu_option_product_detail)
            waitUntilBottomSheetShowingAndSettled {
                findFragmentByTag(context.getString(R.string.change_product_label))
            }
            intendingIntent()
            clickEditProduct()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, EDIT_PRODUCT_PATH)
        }
    }

    @Test
    fun validateClickFilterTime() {
        actionTest {
            clickFilterTime()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, FILTER_TIME_PATH)
        }
    }

    @Test
    fun validateClickFilterStar() {
        actionTest {
            clickFilterStar()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, FILTER_STAR_PATH)
        }
    }

    @Test
    fun validateClickReport() {
        actionTest {
            clickReportDetail()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, REPORT_PATH)
        }
    }

    @Test
    fun validateClickSortFilter() {
        actionTest {
            clickSortFilter()
            clickAction(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, SORT_FILTER_PATH)
        }
    }

    @Test
    fun validateClickQuickFilter() {
        actionTest {
            clickQuickFilter()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, QUICK_FILTER_PATH)
        }
    }

    @Test
    fun validateClickImage() {
        actionTest {
            clickImage()
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, CLICK_IMAGE_PATH)
        }
    }

    private fun skipCoachMark() {
        CoachMarkPreference.setShown(
            context = context,
            tag = SellerReviewDetailFragment.TAG_COACH_MARK_REVIEW_DETAIL,
            hasShown = true
        )
    }

    private fun SellerReviewRobot.clickReportDetail() {
        scrollToRecyclerViewItem(R.id.rvRatingDetail, 4)
        actionOnRecyclerViewItem(
            R.id.rvRatingDetail,
            3,
            CommonActions.clickChildViewWithId(R.id.ivOptionReviewFeedback)
        )
        waitUntilBottomSheetShowingAndSettled {
            findFragmentByTag(context.getString(R.string.option_menu_label))
        }
        intendingIntent()
        waitUntilViewVisible(withId(R.id.optionFeedbackList))
        onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(1)
            .perform(click())
    }

    private fun SellerReviewRobot.clickSortFilter() {
        scrollToRecyclerViewItem(
            R.id.rvRatingDetail,
            getPositionViewHolderByClass<FeedbackUiModel>()
        )
        actionOnRecyclerViewItem(
            R.id.rvRatingDetail,
            getPositionViewHolderByClass<TopicUiModel>(),
            CommonActions.clickChildViewWithId(com.tokopedia.sortfilter.R.id.sort_filter_prefix)
        )
        waitUntilBottomSheetShowingAndSettled(::getPopularTopicsBottomSheet)
        waitUntilViewVisible(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header))
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)).perform(
            ViewActions.swipeUp()
        )
        waitUntilBottomSheetShowingAndSettled(::getPopularTopicsBottomSheet)
        clickRecyclerViewItem(R.id.rvSortFilter, 2)
        clickRecyclerViewItem(R.id.rvTopicFilter, 1)
    }

    private fun SellerReviewRobot.clickQuickFilter() {
        scrollToRecyclerViewItem(R.id.rvRatingDetail, 4)
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

    private fun SellerReviewRobot.clickImage() {
        scrollToRecyclerViewItem(R.id.rvRatingDetail, 4)
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

    private fun SellerReviewRobot.clickFilterStar() {
        actionOnRecyclerViewItem(
            R.id.rvRatingDetail,
            getPositionViewHolderByClass<ProductReviewFilterUiModel>(),
            CommonActions.clickChildViewWithId(R.id.rating_checkbox)
        )
    }

    private fun SellerReviewRobot.clickFilterTime() {
        actionOnRecyclerViewItem(
            R.id.rvRatingDetail,
            0,
            CommonActions.clickChildViewWithId(R.id.review_period_filter_button_detail)
        )
        waitUntilBottomSheetShowingAndSettled {
            findFragmentByTag(context.getString(R.string.title_bottom_sheet_filter))
        }
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

    private fun SellerReviewRobot.clickEditProduct() {
        waitUntilViewVisible(withId(R.id.optionFeedbackList))
        onData(anything()).inAdapterView(withId(R.id.optionFeedbackList)).atPosition(0)
            .perform(click())
    }

    private fun intendingIntent() {
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            ProductReviewDetailOverallResponse::class.java,
            Utils.parseFromJson<ProductReviewDetailOverallResponse>("mockresponse/reviewdetail/get_product_review_initial_usecase/overall.json")
        )
        graphqlRepositoryStub.createMapResult(
            ProductFeedbackDetailResponse::class.java,
            Utils.parseFromJson<ProductFeedbackDetailResponse>("mockresponse/reviewdetail/get_product_review_initial_usecase/feedback_detail_list.json")
        )
        graphqlRepositoryStub.createMapResult(
            ProductFeedbackFilterResponse::class.java,
            Utils.parseFromJson<ProductFeedbackFilterResponse>("mockresponse/reviewdetail/get_product_review_initial_usecase/product_feedback_filter.json")
        )
    }

    private fun getPopularTopicsBottomSheet(): BottomSheetUnify? {
        return activityRule.activity.supportFragmentManager.findFragmentByTag(BaseTopicsBottomSheet.BOTTOM_SHEET_TITLE) as? BottomSheetUnify
    }

    private fun findFragmentByTag(tag: String): BottomSheetUnify? {
        return activityRule.activity.supportFragmentManager.fragments
            .filterIsInstance<SellerReviewDetailFragment>()
            .first().fragmentManager!!.findFragmentByTag(tag) as? BottomSheetUnify
    }
}