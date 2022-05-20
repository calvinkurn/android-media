package com.tokopedia.review.stub.reviewreply.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub
import com.tokopedia.review.stub.reviewreply.di.component.DaggerReviewReplyComponentStub

class SellerReviewReplyActivityStub: SellerReviewReplyActivity() {

    companion object {
        fun createIntent(
            context: Context,
            shopId: String,
            isEmptyReply: Boolean,
            feedbackReplyData: ProductReplyUiModel,
            feedbackData: FeedbackUiModel
        ): Intent {
            return Intent(context, SellerReviewReplyActivityStub::class.java).apply {
                putExtra(SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, feedbackData)
                putExtra(SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, feedbackReplyData)
                putExtra(SellerReviewReplyFragment.EXTRA_SHOP_ID, shopId)
                putExtra(SellerReviewReplyFragment.IS_EMPTY_REPLY_REVIEW, isEmptyReply)
            }
        }
    }

    override fun getComponent(): ReviewReplyComponent {
        return DaggerReviewReplyComponentStub
            .builder()
            .reviewComponentStub(ReviewInstanceStub.getComponent(application))
            .reviewReplyModule(ReviewReplyModule())
            .build()
    }
}