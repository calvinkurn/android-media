package com.tokopedia.review.feature.reviewreply.di.component

import com.tokopedia.review.common.di.ReviewComponent
import com.tokopedia.review.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import dagger.Component

@ReviewReplyScope
@Component(modules = [ReviewReplyModule::class], dependencies = [ReviewComponent::class])
interface ReviewReplyComponent {
    fun inject(sellerReviewReplyFragment: SellerReviewReplyFragment)
}