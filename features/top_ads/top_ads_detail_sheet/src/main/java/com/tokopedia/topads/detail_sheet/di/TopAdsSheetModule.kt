package com.tokopedia.topads.detail_sheet.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.CommonErrorResponseInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

/**
 * Author errysuprayogi on 22,October,2019
 */
@TopAdsSheetScope
@Module
class TopAdsSheetModule {


    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()


    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @TopAdsSheetScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @TopAdsSheetScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main
//
//    @TopAdsSheetScope
//    @Provides
//    fun provideRestRepository(interceptors: MutableList<Interceptor>,
//                              @ApplicationContext context: Context): RestRepository =
//            RestRequestInteractor.getInstance().restRepository.apply {
//                updateInterceptors(interceptors, context)
//            }
//
//    @TopAdsSheetScope
//    @Provides
//    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
//                            loggingInterceptor: HttpLoggingInterceptor,
//                            commonErrorResponseInterceptor: CommonErrorResponseInterceptor) =
//            mutableListOf(tkpdAuthInterceptor, loggingInterceptor, commonErrorResponseInterceptor)
//
//    @TopAdsSheetScope
//    @Provides
//    fun provideAuthInterceptors(@ApplicationContext context: Context,
//                                userSession: UserSessionInterface): TkpdAuthInterceptor {
//        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
//    }
//
//    @TopAdsSheetScope
//    @Provides
//    fun provideErrorInterceptors(): CommonErrorResponseInterceptor {
//        return CommonErrorResponseInterceptor()
//    }
}



