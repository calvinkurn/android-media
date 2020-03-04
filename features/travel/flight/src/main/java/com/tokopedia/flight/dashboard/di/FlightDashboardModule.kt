package com.tokopedia.flight.dashboard.di

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.cachemanager.PersistentCacheManager
import com.tokopedia.common.travel.R
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by alvarisi on 10/26/17.
 */
@Module
class FlightDashboardModule {

    @Provides
    fun provideGetFlightClassesUseCase(flightRepository: FlightRepository): GetFlightClassesUseCase {
        return GetFlightClassesUseCase(flightRepository)
    }

    @Provides
    fun provideFlightDashboardCache(@ApplicationContext context: Context): FlightDashboardCache {
        return FlightDashboardCache(context)
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideCacheManager(@ApplicationContext context: Context): CacheManager = PersistentCacheManager(context)

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.travelcalendar.R.raw.query_get_travel_calendar_holiday)
    }
}
