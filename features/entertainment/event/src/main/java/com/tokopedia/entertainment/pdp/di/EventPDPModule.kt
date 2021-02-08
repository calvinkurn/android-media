package com.tokopedia.entertainment.pdp.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.entertainment.pdp.analytic.EventPDPTracking
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.GetWhiteListValidationUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named

/**
 * Author firman on 06-04-20
 */

@Module
class EventPDPModule {

    @EventPDPScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @EventPDPScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @EventPDPScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @EventPDPScope
    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @EventPDPScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @EventPDPScope
    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.travelcalendar.R.raw.query_get_travel_calendar_holiday)
    }

    @Provides
    @EventPDPScope
    internal fun provideOkHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy()
    }

    @Provides
    @EventPDPScope
    internal fun provideFingerprintInterceptor(networkRouter: NetworkRouter, userSession: UserSessionInterface): FingerprintInterceptor {
        return FingerprintInterceptor(networkRouter, userSession)
    }

    @Provides
    @EventPDPScope
    internal fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @Provides
    @EventPDPScope
    fun provideIris(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }

    @Provides
    @EventPDPScope
    fun provideTracking(irisSession: IrisSession, userSession: UserSessionInterface): EventPDPTracking {
        return EventPDPTracking(userSession, irisSession)
    }

    @Provides
    @EventPDPScope
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @EventPDPScope
    @Provides
    fun provideAuthInterceptors(@ApplicationContext context: Context,
                                userSession: UserSessionInterface): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, context as NetworkRouter, userSession)
    }

    @Provides
    @EventPDPScope
    fun provideInterceptors(tkpdAuthInterceptor: TkpdAuthInterceptor,
                            fingerprintInterceptor: FingerprintInterceptor,
                            httpLoggingInterceptor: HttpLoggingInterceptor,
                            chuckerInterceptor: ChuckerInterceptor): MutableList<Interceptor> {
        return mutableListOf(tkpdAuthInterceptor, fingerprintInterceptor, httpLoggingInterceptor, chuckerInterceptor)
    }

    @Provides
    fun provideRestRepository(interceptors: MutableList<Interceptor>,
                              @ApplicationContext context: Context): RestRepository {
        return RestRequestInteractor.getInstance().restRepository.apply {
            updateInterceptors(interceptors, context)
        }
    }

    @Provides
    @EventPDPScope
    fun provideGetWhiteListValidationUseCase(repository: RestRepository): GetWhiteListValidationUseCase {
        return GetWhiteListValidationUseCase(repository)
    }

    @Provides
    @EventPDPScope
    fun provideGetEventRedeemUseCase(repository: RestRepository): GetEventRedeemUseCase {
        return GetEventRedeemUseCase(repository)
    }

    @Provides
    @EventPDPScope
    fun provideRedeemTicketEventUseCase(repository: RestRepository): RedeemTicketEventUseCase {
        return RedeemTicketEventUseCase(repository)
    }

}