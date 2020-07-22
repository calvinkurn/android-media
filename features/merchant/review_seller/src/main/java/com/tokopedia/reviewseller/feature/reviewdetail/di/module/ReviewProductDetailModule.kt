package com.tokopedia.reviewseller.feature.reviewdetail.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.reviewseller.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@ReviewDetailScope
@Module(includes = [ReviewProductDetailViewModelModule::class])
class ReviewProductDetailModule {

    @ReviewDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewDetailScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewDetailScope
    @Provides
    fun provideProductReviewDetailTracking(): ProductReviewDetailTracking {
        return ProductReviewDetailTracking()
    }

}