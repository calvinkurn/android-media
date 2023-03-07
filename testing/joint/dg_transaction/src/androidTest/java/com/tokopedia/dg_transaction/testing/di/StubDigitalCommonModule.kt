package com.tokopedia.dg_transaction.testing.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.atc.DigitalAddToCartRestUseCase
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase
import com.tokopedia.common_digital.atc.RechargeAddToCartGqlUseCase
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.common_digital.common.di.DigitalCommonScope
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.dg_transaction.testing.response.rest.DigitalRestAddToCartMockResponse
import com.tokopedia.dg_transaction.testing.response.rest.RestRepositoryStub
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor

/** Changes from the original:
 * - add provideRestRepositoryStub() to provide a DigitalAddToCartUseCase mock response
 * - update provideRestRepository & provideDigitalAtcUseCase with RestRepositoryStub */

@Module
class StubDigitalCommonModule {

    @Provides
    @DigitalCommonScope
    fun provideRechargePushEventRecommendationUseCase(@ApplicationContext context: Context): RechargePushEventRecommendationUseCase {
        return RechargePushEventRecommendationUseCase(GraphqlUseCase(), context)
    }

    @Provides
    @DigitalCommonScope
    fun provideRechargeAnalytics(rechargePushEventRecommendationUseCase: RechargePushEventRecommendationUseCase): RechargeAnalytics {
        return RechargeAnalytics(rechargePushEventRecommendationUseCase)
    }

    @DigitalCommonScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @DigitalCommonScope
    @Provides
    fun provideDigitalCartInterceptor(@ApplicationContext context: Context,
                                      networkRouter: NetworkRouter,
                                      userSession: UserSessionInterface
    ): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter, userSession)
    }

    @DigitalCommonScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return if (context is NetworkRouter) context
        else throw RuntimeException("Application must implement " + NetworkRouter::class.java.canonicalName)
    }

    @Provides
    @DigitalCommonScope
    fun provideRestRepositoryStub(): RestRepositoryStub {
        return RestRepositoryStub().apply {
            responses = DigitalRestAddToCartMockResponse().getMockResponse()
        }
    }

    @Provides
    @DigitalCommonScope
    @DigitalAddToCartQualifier
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    @DigitalCommonScope
    @DigitalAddToCartQualifier
    fun provideRestRepository(repositoryStub: RestRepositoryStub): RestRepository = repositoryStub

    @Provides
    @DigitalCommonScope
    fun provideDigitalAtcRestUseCase(@DigitalAddToCartQualifier restRepository: RestRepository)
            : DigitalAddToCartRestUseCase = DigitalAddToCartRestUseCase(restRepository)

    @Provides
    @DigitalCommonScope
    fun provideRechargeAddToCartGqluseCase(@DigitalAddToCartQualifier graphqlRepository: GraphqlRepository)
            : RechargeAddToCartGqlUseCase = RechargeAddToCartGqlUseCase(graphqlRepository)

    @DigitalCommonScope
    @Provides
    fun provideDigitalAtcUseCase(restUseCase: DigitalAddToCartRestUseCase,
                                 gqlUseCase: RechargeAddToCartGqlUseCase
    ): DigitalAddToCartUseCase = DigitalAddToCartUseCase(restUseCase, gqlUseCase)

    @Provides
    @DigitalCommonScope
    @DigitalAddToCartQualifier
    fun provideDigitalInterceptor(digitalInterceptor: DigitalInterceptor): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        return listInterceptor
    }
}