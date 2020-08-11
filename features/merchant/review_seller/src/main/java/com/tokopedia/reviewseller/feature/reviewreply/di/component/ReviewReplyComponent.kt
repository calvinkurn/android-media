package com.tokopedia.reviewseller.feature.reviewreply.di.component

import com.tokopedia.reviewseller.common.di.component.ReviewSellerComponent
import com.tokopedia.reviewseller.feature.reviewreply.di.module.ReviewReplyModule
import com.tokopedia.reviewseller.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.reviewseller.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import dagger.Component

@ReviewReplyScope
@Component(modules = [ReviewReplyModule::class], dependencies = [ReviewSellerComponent::class])
interface ReviewReplyComponent {
    fun inject(sellerReviewReplyFragment: SellerReviewReplyFragment)
}