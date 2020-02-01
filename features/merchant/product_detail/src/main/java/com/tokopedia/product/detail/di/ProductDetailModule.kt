package com.tokopedia.product.detail.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.affiliatecommon.di.AffiliateCommonModule
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.RawQueryKeyConstant.QUERY_RECOMMEN_PRODUCT
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProviderImpl
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@ProductDetailScope
@Module (includes = [ProductRestModule::class, AffiliateCommonModule::class])
class ProductDetailModule {

    @ProductDetailScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = Interactor.getInstance().graphqlRepository

    @ProductDetailScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ProductDetailScope
    @Provides
    fun provideDispatcherProvider(): DynamicProductDetailDispatcherProvider = DynamicProductDetailDispatcherProviderImpl()

    @ProductDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @ProductDetailScope
    @Provides
    fun provideProductDetailTracking(trackingQueue: TrackingQueue) = ProductDetailTracking(trackingQueue)

    @ProductDetailScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @ProductDetailScope
    @Provides
    fun provideStickyLoginUseCase(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository): StickyLoginUseCase {
        return StickyLoginUseCase(context.resources, graphqlRepository)
    }

    @ProductDetailScope
    @Provides
    fun provideGetRecommendationUseCase(rawQueries: Map<String, String>,
                                        graphqlUseCase: GraphqlUseCase,
                                        userSessionInterface: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(rawQueries[QUERY_RECOMMEN_PRODUCT]?:"", graphqlUseCase, userSessionInterface)
    }
}