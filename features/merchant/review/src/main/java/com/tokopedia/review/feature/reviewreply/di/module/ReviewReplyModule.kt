package com.tokopedia.review.feature.reviewreply.di.module

import com.tokopedia.review.feature.reviewreply.analytics.SellerReviewReplyTracking
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import dagger.Module
import dagger.Provides

@Module(includes = [ReviewReplyViewModelModule::class])
class ReviewReplyModule {

    @ReviewReplyScope
    @Provides
    fun provideProductReviewTracking(): SellerReviewReplyTracking {
        return SellerReviewReplyTracking()
    }
}