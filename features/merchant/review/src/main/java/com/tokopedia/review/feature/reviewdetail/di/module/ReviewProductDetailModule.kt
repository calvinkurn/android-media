package com.tokopedia.review.feature.reviewdetail.di.module

import com.tokopedia.review.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.review.feature.reviewdetail.di.scope.ReviewDetailScope
import dagger.Module
import dagger.Provides

@Module(includes = [ReviewProductDetailViewModelModule::class])
class ReviewProductDetailModule {

    @ReviewDetailScope
    @Provides
    fun provideProductReviewDetailTracking(): ProductReviewDetailTracking {
        return ProductReviewDetailTracking()
    }

}