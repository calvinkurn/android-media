package com.tokopedia.reviewseller.feature.reviewlist.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProvider
import com.tokopedia.reviewseller.common.util.CoroutineDispatcherProviderImpl
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_RATING_OVERALL
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_REVIEW_LIST
import com.tokopedia.reviewseller.feature.reviewlist.di.scope.ReviewProductListScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

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