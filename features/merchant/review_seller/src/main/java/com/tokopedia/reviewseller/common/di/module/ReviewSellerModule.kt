package com.tokopedia.reviewseller.common.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.di.scope.ReviewSellerScope
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_RATING_OVERALL
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_REVIEW_LIST
import com.tokopedia.reviewseller.feature.reviewlist.analytics.ProductReviewTracking
import com.tokopedia.reviewseller.feature.reviewlist.di.module.ReviewProductListViewModelModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

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
    @Named(GQL_GET_PRODUCT_RATING_OVERALL)
    fun getProductRatingOverall(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_rating_overall)
    }

    @ReviewSellerScope
    @Provides
    @Named(GQL_GET_PRODUCT_REVIEW_LIST)
    fun getProductReviewList(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_review_list)
    }

    @ReviewSellerScope
    @Provides
    fun provideProductReviewTracking(): ProductReviewTracking {
        return ProductReviewTracking()
    }

}