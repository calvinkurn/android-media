package com.tokopedia.promocheckout.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.promocheckout.common.domain.*
import com.tokopedia.promocheckout.common.domain.digital.DigitalCheckVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase
import com.tokopedia.promocheckout.common.domain.flight.FlightCheckVoucherUseCase
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

@Module
class PromoCheckoutModule {

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelPromoUseCase(@PromoCheckoutQualifier listInterceptor : ArrayList<Interceptor>, @ApplicationContext context: Context): CancelPromoUseCase {
        return CancelPromoUseCase(listInterceptor, context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoCodeUseCase(@ApplicationContext context: Context): CheckPromoCodeUseCase {
        return CheckPromoCodeUseCase(context.resources, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoStackingCodeUseCase(@ApplicationContext context: Context): CheckPromoStackingCodeUseCase {
        return CheckPromoStackingCodeUseCase(context.resources)
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
    fun provideCheckVoucherFlightUseCase(@ApplicationContext context: Context): FlightCheckVoucherUseCase {
        return FlightCheckVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCancelVoucherDigitalUseCase(@ApplicationContext context: Context): FlightCancelVoucherUseCase {
        return FlightCancelVoucherUseCase(context, GraphqlUseCase())
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context, @PromoCheckoutQualifier networkRouter: NetworkRouter,
                                   @PromoCheckoutQualifier userSession: UserSession) : ArrayList<Interceptor>{
        val  listInterceptor =  ArrayList<Interceptor>()
        listInterceptor.add(TkpdAuthInterceptor(context, networkRouter, userSession))
        if(GlobalConfig.DEBUG){
            listInterceptor.add(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return listInterceptor
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context) : NetworkRouter{
        return (context as NetworkRouter)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideUserSession(@ApplicationContext context: Context) : UserSession {
        val userSession = UserSession(context)
        return userSession
    }

}