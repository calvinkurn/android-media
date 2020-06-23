package com.tokopedia.review.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.review.common.util.CoroutineDispatcherProvider
import com.tokopedia.review.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.review.feature.reviewlist.analytics.ProductReviewTracking
import com.tokopedia.review.feature.reviewlist.di.module.ReviewProductListViewModelModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module(includes = [ReviewProductListViewModelModule::class])
@ReviewSellerScope
class ReviewSellerModule {

    @ReviewSellerScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @ReviewSellerScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ReviewSellerScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ReviewSellerScope
    @Provides
    fun provideMultipleRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @ReviewSellerScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewSellerScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewSellerScope
    @Provides
    fun provideProductReviewTracking(): ProductReviewTracking {
        return ProductReviewTracking()
    }

}