package com.tokopedia.review.analytics.seller.reviewreply

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.review.R
import com.tokopedia.review.analytics.common.CassavaTestFixture
import com.tokopedia.review.analytics.common.actionTest
import com.tokopedia.review.common.Utils
import com.tokopedia.review.feature.reviewreply.update.domain.model.ReviewReplyUpdateResponse
import com.tokopedia.review.feature.inboxreview.domain.mapper.InboxReviewMapper
import com.tokopedia.review.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.data.ReviewReplyTemplateListResponse
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.review.stub.reviewreply.view.activity.SellerReviewReplyActivityStub
import com.tokopedia.unifycomponents.BottomSheetUnify
import org.junit.Rule
import org.junit.Test

class SellerReviewReplyCassavaTest : CassavaTestFixture() {

    companion object {
        const val TRACKER_CLICK_RESPONSE_REVIEW_INPUT_FIELD = "tracker/merchant/review/seller/review_reply_click_response_review_input_field.json"
        const val TRACKER_CLICK_ADD_TEMPLATE_REVIEW_RESPONSE = "tracker/merchant/review/seller/review_reply_click_add_template_review_response.json"
        const val TRACKER_CLICK_SAVED_TEMPLATE_REVIEW_RESPONSE = "tracker/merchant/review/seller/review_reply_click_saved_template_review_response.json"
        const val TRACKER_CLICK_SEND_BUTTON_TO_REPLY_REVIEW = "tracker/merchant/review/seller/review_reply_click_send_button_to_reply_review.json"
        const val TRACKER_CLICK_THREE_DOTS_MENU = "tracker/merchant/review/seller/review_reply_click_three_dots_menu.json"
        const val TRACKER_CLICK_REPORT_REVIEW_FROM_THREE_DOTS_MENU = "tracker/merchant/review/seller/review_reply_click_report_review_from_three_dots_menu.json"
        const val TRACKER_CLICK_EDIT_ON_REVIEW_RESPONSE = "tracker/merchant/review/seller/review_reply_click_edit_on_review_response.json"
        const val MOCK_RESPONSE_INBOX_REVIEW = "mockresponse/inboxreview/get_inbox_review_usecase/inbox_review.json"
        const val MOCK_RESPONSE_REVIEW_REPLY_TEMPLATE_LIST = "mockresponse/reviewreply/get_review_template_list_usecase/review_reply_template_list.json"
        const val MOCK_RESPONSE_REVIEW_REPLY_UPDATE_REPLY = "mockresponse/reviewreply/update_seller_response_usecase/review_reply_update.json"
    }

    @get:Rule
    var activityRule = IntentsTestRule(SellerReviewReplyActivityStub::class.java, false, false)

    private val mockFeedbackItem: FeedbackInboxUiModel by lazy {
        Utils.parseFromJson<InboxReviewResponse>(
            filePath = MOCK_RESPONSE_INBOX_REVIEW
        ).run {
            InboxReviewMapper.mapToInboxReviewUiModel(
                inboxReviewResponse = productrevGetInboxReviewByShop,
                userSession = userSession
            ).feedbackInboxList.first()
        }
    }
    private val mockProductReplyUiModel: ProductReplyUiModel by lazy {
        ProductReplyUiModel(
            mockFeedbackItem.productID,
            mockFeedbackItem.productImageUrl,
            mockFeedbackItem.productName,
            mockFeedbackItem.variantName
        )
    }
    private val mockFeedbackUiModel: FeedbackUiModel by lazy {
        InboxReviewMapper.mapFeedbackInboxToFeedbackUiModel(mockFeedbackItem)
    }
    private val cacheManager: SaveInstanceCacheManager by lazy {
        SaveInstanceCacheManager(context = context, generateObjectId = true).apply {
            put(SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, mockFeedbackUiModel)
            put(SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, mockProductReplyUiModel)
        }
    }

    override fun setup() {
        super.setup()
        setAppToSellerApp()
        mockResponses()
        val intent = SellerReviewReplyActivityStub.createIntent(
            context,
            cacheManager.id.orEmpty(),
            userSession.shopId,
            mockFeedbackItem.replyText.isBlank()
        )
        activityRule.launchActivity(intent)
    }

    @Test
    fun testClickResponseReviewInputField() {
        actionTest {
            clickAction(R.id.replyEditText)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_RESPONSE_REVIEW_INPUT_FIELD)
        }
    }

    @Test
    fun testClickAddTemplateReview() {
        actionTest {
            clickAction(R.id.btnAddTemplate)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_ADD_TEMPLATE_REVIEW_RESPONSE)
        }
    }

    @Test
    fun testClickSavedTemplateReviewResponse() {
        actionTest {
            clickRecyclerViewItem(R.id.list_template, 0)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_SAVED_TEMPLATE_REVIEW_RESPONSE)
        }
    }

    @Test
    fun testClickSendReviewReply() {
        actionTest {
            clickRecyclerViewItem(R.id.list_template, 0)
            clickAction(R.id.replySendButton)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_SEND_BUTTON_TO_REPLY_REVIEW)
        }
    }

    @Test
    fun testClickThreeDotsMenu() {
        actionTest {
            clickAction(R.id.menu_option_product_detail)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_THREE_DOTS_MENU)
        }
    }

    @Test
    fun testClickItemReportOnBottomSheet() {
        actionTest {
            clickAction(R.id.menu_option_product_detail)
            waitUntilBottomSheetShowingAndSettled(::getBottomSheetReplyReview)
            clickAction(context.getString(R.string.report_label))
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_REPORT_REVIEW_FROM_THREE_DOTS_MENU)
        }
    }

    @Test
    fun testClickEditReviewResponse() {
        actionTest {
            clickAction(R.id.tvReplyEdit)
        } assertTest {
            performClose(activityRule)
            validate(cassavaTestRule, TRACKER_CLICK_EDIT_ON_REVIEW_RESPONSE)
        }
    }

    private fun mockResponses() {
        graphqlRepositoryStub.createMapResult(
            ReviewReplyTemplateListResponse::class.java,
            Utils.parseFromJson<ReviewReplyTemplateListResponse>(
                MOCK_RESPONSE_REVIEW_REPLY_TEMPLATE_LIST
            )
        )
        graphqlRepositoryStub.createMapResult(
            ReviewReplyUpdateResponse::class.java,
            Utils.parseFromJson<ReviewReplyUpdateResponse>(
                MOCK_RESPONSE_REVIEW_REPLY_UPDATE_REPLY
            )
        )
    }

    private fun getBottomSheetReplyReview(): BottomSheetUnify? {
        return activityRule.activity.supportFragmentManager.fragments
            .filterIsInstance<SellerReviewReplyFragment>()
            .first()
            .childFragmentManager
            .fragments
            .filterIsInstance<BottomSheetUnify>()
            .first()
    }
}