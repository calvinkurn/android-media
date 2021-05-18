package com.tokopedia.common_digital.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.atc.DigitalAddToCartUseCase
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.usecase.RechargePushEventRecommendationUseCase
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor

/**
 * Created by Rizky on 13/08/18.
 */
@Module
class DigitalCommonModule {

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
                                      userSession: UserSessionInterface): DigitalInterceptor {
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
    @DigitalAddToCartQualifier
    fun provideRestRepository(@DigitalAddToCartQualifier interceptors: ArrayList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @DigitalCommonScope
    @Provides
    fun provideDigitalAtcUseCase(@DigitalAddToCartQualifier restRepository: RestRepository)
            : DigitalAddToCartUseCase = DigitalAddToCartUseCase(restRepository)

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
