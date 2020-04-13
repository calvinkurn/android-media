package com.tokopedia.reviewseller.feature.reviewlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.GQL_GET_PRODUCT_RATING_OVERALL
import com.tokopedia.reviewseller.common.GQL_GET_PRODUCT_REVIEW_LIST
import com.tokopedia.reviewseller.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.reviewseller.feature.reviewlist.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.feature.reviewlist.util.CoroutineDispatcherProviderImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module(includes = [ReviewSellerViewModelModule::class])
@ReviewProductListScope
class ReviewProductListModule {

    @ReviewProductListScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewProductListScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ReviewProductListScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ReviewProductListScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewProductListScope
    @Provides
    @Named(GQL_GET_PRODUCT_RATING_OVERALL)
    fun getProductRatingOverall(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_rating_overall)
    }

    @ReviewProductListScope
    @Provides
    @Named(GQL_GET_PRODUCT_REVIEW_LIST)
    fun getProductReviewList(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_review_list)
    }
}