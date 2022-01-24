package com.tokopedia.review.stub.reviewreply.view.activity

import android.content.Context
import android.content.Intent
import com.tokopedia.review.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.stub.reviewcommon.ReviewInstanceStub
import com.tokopedia.review.stub.reviewreply.di.component.DaggerReviewReplyComponentStub

class SellerReviewReplyActivityStub: SellerReviewReplyActivity() {

    companion object {
        fun createIntent(context: Context, cacheManagerId: String, shopId: String, isEmptyReply: Boolean): Intent {
            return Intent(context, SellerReviewReplyActivityStub::class.java).apply {
                putExtra(SellerReviewReplyFragment.CACHE_OBJECT_ID, cacheManagerId)
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