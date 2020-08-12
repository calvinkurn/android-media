package com.tokopedia.reviewseller.feature.reviewlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.feature.reviewlist.analytics.ProductReviewTracking
import com.tokopedia.reviewseller.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@ReviewProductListScope
@Module(includes = [ReviewProductListViewModelModule::class])
class ReviewProductListModule {

    @ReviewProductListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewProductListScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewProductListScope
    @Provides
    fun provideProductReviewTracking(): ProductReviewTracking {
        return ProductReviewTracking()
    }
}