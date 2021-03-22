package com.tokopedia.digital_checkout.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse
import com.tokopedia.digital_checkout.utils.analytics.DigitalAnalytics
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
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
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.IO

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
    @DigitalCheckoutScope
    @DigitalCartCheckoutQualifier
    fun provideCheckoutRestRepository(@DigitalCartCheckoutQualifier interceptors: ArrayList<Interceptor>,
                                      @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @DigitalCheckoutScope
    @Provides
    fun provideDigitalAnalytics(): DigitalAnalytics = DigitalAnalytics()

    @DigitalCheckoutScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()
}