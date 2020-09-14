package com.tokopedia.travelcalendar.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travelcalendar.data.TravelCalendarGQLQuery
import com.tokopedia.travelcalendar.domain.TravelCalendarProvider
import com.tokopedia.travelcalendar.domain.TravelCalendarScheduler
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@Module
class TravelCalendarModule {

    @Provides
    fun provideTravelCalendarProvider(): TravelCalendarProvider {
        return TravelCalendarScheduler()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(): String = TravelCalendarGQLQuery.GET_TRAVEL_CALENDAR_HOLIDAY
}
