package com.tokopedia.promocheckout.common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutRouter
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.domain.CancelPromoUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeFinalUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeUseCase
import com.tokopedia.promocheckout.common.domain.CheckPromoStackingCodeUseCase
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
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
        return CheckPromoStackingCodeUseCase(context)
    }

    @PromoCheckoutQualifier
    @Provides
    fun provideCheckPromoCodeFinalUseCase(@ApplicationContext context: Context): CheckPromoCodeFinalUseCase {
        return CheckPromoCodeFinalUseCase(context.resources, GraphqlUseCase())
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