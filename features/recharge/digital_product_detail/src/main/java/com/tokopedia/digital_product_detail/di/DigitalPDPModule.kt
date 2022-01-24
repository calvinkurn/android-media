package com.tokopedia.digital_product_detail.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.topupbills.analytics.CommonTopupBillsAnalytics
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.common.di.DigitalAddToCartQualifier
import com.tokopedia.common_digital.common.di.DigitalCommonScope
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_product_detail.presentation.utils.DigitalPDPTelcoAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author by firmanda on 04/01/21
 */

@Module
class DigitalPDPModule {

    @DigitalPDPScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @DigitalPDPScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @DigitalPDPScope
    @Provides
    fun provideDigitalPDPTelcoAnalytics(): DigitalPDPTelcoAnalytics {
        return DigitalPDPTelcoAnalytics()
    }

    @DigitalPDPScope
    @Provides
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @DigitalPDPScope
    @Provides
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @DigitalPDPScope
    @Provides
    fun provideDigitalInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSessionInterface
    ): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter, userSession)
    }


    @DigitalPDPScope
    @Provides
    fun provideAnalyticsCommon(): CommonTopupBillsAnalytics {
        return CommonTopupBillsAnalytics()
    }

    @Provides
    @DigitalPDPScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @DigitalPDPScope
    fun provideInterceptors(fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            digitalInterceptor: DigitalInterceptor,
                            chuckerInterceptor: ChuckerInterceptor
    ): MutableList<Interceptor> {
        val listInterceptor = mutableListOf<Interceptor>()
        listInterceptor.add(fingerprintInterceptor)
        listInterceptor.add(digitalInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            listInterceptor.add(chuckerInterceptor)
            listInterceptor.add(httpLoggingInterceptor)
        }
        return listInterceptor
    }

    @Provides
    @DigitalPDPScope
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @DigitalPDPScope
    fun providePermissionCheckerHelper(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @Provides
    @DigitalPDPScope
    @DigitalAddToCartQualifier
    fun provideRestRepository(@DigitalAddToCartQualifier interceptors: ArrayList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @DigitalPDPScope
    @DigitalAddToCartQualifier
    fun provideDigitalInterceptors(digitalInterceptor: DigitalInterceptor): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        return listInterceptor
    }
}