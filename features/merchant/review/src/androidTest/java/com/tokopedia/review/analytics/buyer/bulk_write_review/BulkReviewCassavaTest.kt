package com.tokopedia.review.analytics.buyer.bulk_write_review

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.CassavaTestFixture
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.common.Utils
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewGetFormResponse
import com.tokopedia.review.feature.bulk_write_review.domain.model.BulkReviewSubmitResponse
import com.tokopedia.review.feature.createreputation.model.BadRatingCategoriesResponse
import com.tokopedia.review.stub.bulk_write_review.presentation.activity.BulkReviewActivityStub
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import org.junit.Rule
import org.junit.Test

class BulkReviewCassavaTest : CassavaTestFixture() {

    companion object {
        const val TRACKER_REVIEW_ITEM_IMPRESSION =
            "tracker/merchant/review/feature/bulk_write_review/review_item_impression.json"
        const val TRACKER_SUBMISSION =
            "tracker/merchant/review/feature/bulk_write_review/submission.json"
        const val TRACKER_DISMISSAL =
            "tracker/merchant/review/feature/bulk_write_review/dismissal.json"
        const val TRACKER_BAD_RATING_CATEGORY_IMPRESSION =
            "tracker/merchant/review/feature/bulk_write_review/bad_rating_impression.json"
        const val TRACKER_BAD_RATING_CATEGORY_SELECTION =
            "tracker/merchant/review/feature/bulk_write_review/bad_rating_selection.json"
        const val TRACKER_SUBMISSION_ERROR =
            "tracker/merchant/review/feature/bulk_write_review/submission_error.json"
        const val TRACKER_REMOVE_REVIEW_ITEM_CLICK =
            "tracker/merchant/review/feature/bulk_write_review/remove_review_item_icon_click.json"
        const val TRACKER_REMOVE_REVIEW_ITEM_DIALOG_IMPRESSION =
            "tracker/merchant/review/feature/bulk_write_review/remove_review_item_dialog_impression.json"
        const val TRACKER_REMOVE_REVIEW_ITEM_DIALOG_CANCEL =
            "tracker/merchant/review/feature/bulk_write_review/remove_review_item_dialog_cancel.json"
        const val TRACKER_REMOVE_REVIEW_ITEM_DIALOG_CONFIRM =
            "tracker/merchant/review/feature/bulk_write_review/remove_review_item_dialog_confirm.json"
        const val TRACKER_ADD_TESTIMONY_MINI_ACTION_CLICK =
            "tracker/merchant/review/feature/bulk_write_review/review_item_add_testimony_mini_action_click.json"
        const val TRACKER_ADD_ATTACHMENT_MINI_ACTION_CLICK =
            "tracker/merchant/review/feature/bulk_write_review/review_item_add_attachment_mini_action_click.json"
        const val TRACKER_CHANGE_RATING =
            "tracker/merchant/review/feature/bulk_write_review/review_item_change_rating.json"
        const val MOCK_RESPONSE_SUCCESS_GET_FORM =
            "mockresponse/bulk_write_review/get_form_usecase/get_form_use_case_result_success.json"
        const val MOCK_RESPONSE_SUCCESS_GET_BAD_RATING_CATEGORY =
            "mockresponse/bulk_write_review/get_bad_rating_category_usecase/get_bad_rating_category_use_case_result_success.json"
        const val MOCK_RESPONSE_SUCCESS_SUBMIT_ALL =
            "mockresponse/bulk_write_review/submit_usecase/submit_use_case_result_all_success.json"
        const val MOCK_RESPONSE_ERROR_SUBMIT_ALL =
            "mockresponse/bulk_write_review/submit_usecase/submit_use_case_result_all_error.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(BulkReviewActivityStub::class.java, false, false)

    override fun setup() {
        super.setup()
        mockResponses()
        activityRule.launchActivity(Intent(context, BulkReviewActivityStub::class.java))
    }

    @Test
    fun testImpressReviewItemTracker() {
        actionTest {

        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_REVIEW_ITEM_IMPRESSION)
        }
    }

    @Test
    fun testSubmissionTracker() {
        graphqlRepositoryStub.createMapResult(
            BulkReviewSubmitResponse.Data::class.java,
            Utils.parseFromJson<BulkReviewSubmitResponse.Data>(MOCK_RESPONSE_SUCCESS_SUBMIT_ALL)
        )
        actionTest {
            blockAllIntent()
            clickAction(R.id.btn_bulk_review_send)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_SUBMISSION)
        }
    }

    @Test
    fun testDismissalTracker() {
        actionTest {
            performBack()
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_DISMISSAL)
        }
    }

    @Test
    fun testBadRatingCategoryImpressionTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_3_create_review)
            )
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_BAD_RATING_CATEGORY_IMPRESSION)
        }
    }

    @Test
    fun testBadRatingCategorySelectionTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_3_create_review)
            )
            clickRecyclerViewItem(R.id.rv_bulk_review_bad_rating_category, 0)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_BAD_RATING_CATEGORY_SELECTION)
        }
    }

    @Test
    fun testSubmissionErrorTracker() {
        graphqlRepositoryStub.createMapResult(
            BulkReviewSubmitResponse.Data::class.java,
            Utils.parseFromJson<BulkReviewSubmitResponse.Data>(MOCK_RESPONSE_ERROR_SUBMIT_ALL)
        )
        actionTest {
            clickAction(R.id.btn_bulk_review_send)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_SUBMISSION_ERROR)
        }
    }

    @Test
    fun testRemoveReviewItemIconClickTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(R.id.ic_bulk_review_remove)
            )
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_REMOVE_REVIEW_ITEM_CLICK)
        }
    }

    @Test
    fun testRemoveReviewItemDialogImpressionTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_4_create_review)
            )
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(R.id.ic_bulk_review_remove)
            )
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_REMOVE_REVIEW_ITEM_DIALOG_IMPRESSION)
        }
    }

    @Test
    fun testRemoveReviewItemDialogCancelTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_4_create_review)
            )
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(R.id.ic_bulk_review_remove)
            )
            clickAction(com.tokopedia.dialog.R.id.dialog_btn_secondary)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_REMOVE_REVIEW_ITEM_DIALOG_CANCEL)
        }
    }

    @Test
    fun testRemoveReviewItemDialogConfirmTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_4_create_review)
            )
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(R.id.ic_bulk_review_remove)
            )
            clickAction(com.tokopedia.dialog.R.id.dialog_btn_primary)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_REMOVE_REVIEW_ITEM_DIALOG_CONFIRM)
        }
    }

    @Test
    fun testAddTestimonyMiniActionClickTracker() {
        actionTest {
            // remove all review item so that there will be only 1 review item to make mini action click easier
            for (i in 10 downTo 2) {
                actionOnRecyclerViewItem(
                    R.id.rv_bulk_review_items,
                    i,
                    clickChildViewWithId(R.id.ic_bulk_review_remove)
                )
            }
            clickRecyclerViewItem(R.id.rv_bulk_review_mini_action, 0)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_ADD_TESTIMONY_MINI_ACTION_CLICK)
        }
    }

    @Test
    fun testAddAttachmentMiniActionClickTracker() {
        actionTest {
            blockAllIntent()
            // remove all review item so that there will be only 1 review item to make mini action click easier
            for (i in 10 downTo 2) {
                actionOnRecyclerViewItem(
                    R.id.rv_bulk_review_items,
                    i,
                    clickChildViewWithId(R.id.ic_bulk_review_remove)
                )
            }
            clickRecyclerViewItem(R.id.rv_bulk_review_mini_action, 1)
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_ADD_ATTACHMENT_MINI_ACTION_CLICK)
        }
    }

    @Test
    fun testChangeRatingTracker() {
        actionTest {
            actionOnRecyclerViewItem(
                R.id.rv_bulk_review_items,
                1,
                clickChildViewWithId(com.tokopedia.reputation.common.R.id.anim_4_create_review)
            )
        } assertTest {
            performClose(activityRule)
            validateWithTimeout(cassavaTestRule, TRACKER_CHANGE_RATING)
        }
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            BulkReviewGetFormResponse.Data::class.java,
            Utils.parseFromJson<BulkReviewGetFormResponse.Data>(MOCK_RESPONSE_SUCCESS_GET_FORM)
        )
        graphqlRepositoryStub.createMapResult(
            BadRatingCategoriesResponse::class.java,
            Utils.parseFromJson<BadRatingCategoriesResponse>(
                MOCK_RESPONSE_SUCCESS_GET_BAD_RATING_CATEGORY
            )
        )
    }
}
