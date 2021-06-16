package com.tokopedia.smartbills.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.smartbills.analytics.SmartBillsAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class SmartBillsModule {

    @SmartBillsScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SmartBillsScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SmartBillsScope
    @Provides
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @SmartBillsScope
    @Provides
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @SmartBillsScope
    @Provides
    fun provideDigitalInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSessionInterface): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter, userSession)
    }

    @SmartBillsScope
    @Provides
    fun provideAnalytics(): SmartBillsAnalytics {
        return SmartBillsAnalytics()
    }

    @Provides
    @SmartBillsScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @Provides
    @SmartBillsScope
    fun provideInterceptors(fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            digitalInterceptor: DigitalInterceptor,
                            chuckerInterceptor: ChuckerInterceptor): MutableList<Interceptor> {
        val listInterceptor = mutableListOf<Interceptor>()
        listInterceptor.add(fingerprintInterceptor)
        listInterceptor.add(httpLoggingInterceptor)
        listInterceptor.add(digitalInterceptor)

        if (GlobalConfig.isAllowDebuggingTools()) {
            listInterceptor.add(chuckerInterceptor)
        }
        return listInterceptor
    }

    @Provides
    @SmartBillsScope
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }
}