package com.tokopedia.review.feature.reviewlist.di.module

import com.tokopedia.review.feature.reviewlist.analytics.ProductReviewTracking
import com.tokopedia.review.feature.reviewlist.di.scope.ReviewProductListScope
import dagger.Module
import dagger.Provides

@Module
class ReviewProductListModule {

    @ReviewProductListScope
    @Provides
    fun provideProductReviewTracking(): ProductReviewTracking {
        return ProductReviewTracking()
    }
}