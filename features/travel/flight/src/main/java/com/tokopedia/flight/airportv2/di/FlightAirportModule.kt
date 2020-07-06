package com.tokopedia.flight.airportv2.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.airportv2.domain.FlightAirportPopularCityUseCase
import com.tokopedia.flight.airportv2.domain.FlightAirportSuggestionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@FlightAirportScope
@Module
class FlightAirportModule {

    @Provides
    @FlightAirportScope
    @Named(FlightAirportPopularCityUseCase.NAMED_FLIGHT_AIRPORT_POPULAR_CITY_QUERY)
    fun provideFlightAirportPopularCityQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_airport_popular_city)

    @Provides
    @FlightAirportScope
    @Named(FlightAirportSuggestionUseCase.NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY)
    fun provideFlightAirportSuggestionQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_airport_suggestion)

}
