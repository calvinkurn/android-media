package com.tokopedia.review.stub.reviewreply.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub
import com.tokopedia.review.stub.reviewreply.di.component.DaggerReviewReplyComponentStub
import com.tokopedia.reviewcommon.extension.put

class SellerReviewReplyActivityStub: SellerReviewReplyActivity() {

    companion object {
        fun createIntent(
            context: Context,
            shopId: String,
            isEmptyReply: Boolean,
            feedbackReplyData: ProductReplyUiModel,
            feedbackData: FeedbackUiModel
        ): Intent {
            val gson = ReviewReplyModule().provideGson()
            val cacheManager = SaveInstanceCacheManager(
                context = context,
                generateObjectId = true
            ).apply {
                put(customId = SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, objectToPut = feedbackData, gson = gson)
                put(customId = SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, objectToPut = feedbackReplyData, gson = gson)
                put(customId = SellerReviewReplyFragment.EXTRA_SHOP_ID, objectToPut = shopId, gson = gson)
                put(customId = SellerReviewReplyFragment.IS_EMPTY_REPLY_REVIEW, objectToPut = isEmptyReply, gson = gson)
            }

            return Intent(context, SellerReviewReplyActivityStub::class.java).apply {
                putExtra(SellerReviewReplyFragment.CACHE_OBJECT_ID, cacheManager.id)
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