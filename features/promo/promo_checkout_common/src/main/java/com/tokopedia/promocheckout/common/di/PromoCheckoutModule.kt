package com.tokopedia.promocheckout.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.domain.umroh.UmrahCheckPromoUseCase
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor


@Module
class PromoCheckoutModule {

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelPromoUseCase(@PromoCheckoutQualifier listInterceptor: ArrayList<Interceptor>, @ApplicationContext context: Context): CancelPromoUseCase {
        return CancelPromoUseCase(listInterceptor, context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoCodeUseCase(@ApplicationContext context: Context): CheckPromoCodeUseCase {
        return CheckPromoCodeUseCase(context.resources, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context,
                                             mapper: CheckPromoStackingCodeMapper): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources, mapper)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoStackingCodeFinalUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeFinalUseCase {
        return CheckPromoStackingCodeFinalUseCase(context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckVoucherDigitalUseCase(@ApplicationContext context: Context): DigitalCheckVoucherUseCase {
        return DigitalCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckVoucherFlightUseCase(): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelVoucherDigitalUseCase(): FlightCancelVoucherUseCase {
        return FlightCancelVoucherUseCase(GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideUmrahCheckPromoUseCase(@ApplicationContext context: Context): UmrahCheckPromoUseCase {
        return UmrahCheckPromoUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, @PromoCheckoutQualifier networkRouter: NetworkRouter,
                                   @PromoCheckoutQualifier userSession: UserSession): ArrayList<Interceptor> {
        val listInterceptor = ArrayList<Interceptor>()
        listInterceptor.add(TkpdAuthInterceptor(context, networkRouter, userSession))
        if (GlobalConfig.DEBUG) {
            listInterceptor.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return listInterceptor
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return (context as NetworkRouter)
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSession {
        val userSession = UserSession(context)
        return userSession
    }
}