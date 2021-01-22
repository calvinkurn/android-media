package com.tokopedia.digital_checkout.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.NetworkRouter
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor

/**
 * @author by jessica on 07/01/21
 */

@Module
class DigitalCheckoutModule {

    @DigitalCheckoutScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @DigitalCheckoutScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @DigitalCheckoutScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DigitalCheckoutScope
    @DigitalCartQualifier
    fun provideDigitalInterceptor(digitalInterceptor: DigitalInterceptor): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        return listInterceptor
    }

    @Provides
    @DigitalCheckoutScope
    @DigitalCartCheckoutQualifier
    fun provideDigitalCheckoutInterceptor(akamaiBotInterceptor: AkamaiBotInterceptor,
                                          digitalInterceptor: DigitalInterceptor): ArrayList<Interceptor> {
        val listInterceptor = arrayListOf<Interceptor>()
        listInterceptor.add(digitalInterceptor)
        listInterceptor.add(ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse::class.java))
        listInterceptor.add(akamaiBotInterceptor)
        return listInterceptor
    }

    @Provides
    fun provideDigitalAkamaiInterceptor(@ApplicationContext context: Context): AkamaiBotInterceptor {
        return AkamaiBotInterceptor(context)
    }

    @Provides
    fun provideDigitalCartInterceptor(@ApplicationContext context: Context,
                                  networkRouter: NetworkRouter,
                                  userSession: UserSessionInterface): DigitalInterceptor {
        return DigitalInterceptor(context, networkRouter, userSession)
    }

    @DigitalCheckoutScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return if (context is NetworkRouter) context
        else throw RuntimeException("Application must implement " + NetworkRouter::class.java.canonicalName)
    }
}