package com.tokopedia.review.feature.reviewreply.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.review.feature.reviewreply.analytics.SellerReviewReplyTracking
import com.tokopedia.review.feature.reviewreply.di.scope.ReviewReplyScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@ReviewReplyScope
@Module(includes = [ReviewReplyViewModelModule::class])
class ReviewReplyModule {

    @ReviewReplyScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewReplyScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewReplyScope
    @Provides
    fun provideProductReviewTracking(): SellerReviewReplyTracking {
        return SellerReviewReplyTracking()
    }
}