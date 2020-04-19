package com.tokopedia.reviewseller.feature.reviewdetail.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL
import com.tokopedia.reviewseller.common.GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.feature.reviewdetail.di.scope.ReviewDetailScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@ReviewDetailScope
@Module(includes = [ReviewProductDetailViewModelModule::class])
class ReviewProductDetailModule {

    @ReviewDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ReviewDetailScope
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ReviewDetailScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ReviewDetailScope
    @Provides
    fun getCoroutineDispatcherProvider(): CoroutineDispatcherProvider {
        return CoroutineDispatcherProviderImpl
    }

    @ReviewDetailScope
    @Provides
    @Named(GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL)
    fun getProductReviewDetailOverall(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_review_detail_overall)
    }

    @ReviewDetailScope
    @Provides
    @Named(GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL)
    fun getProductFeedbackListDetail(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.gql_product_feedback_list_detail)
    }
}