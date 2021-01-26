package com.tokopedia.flight.airportv2.di

import com.tokopedia.flight.airportv2.data.FlightAirportQuery
import com.tokopedia.flight.airportv2.domain.FlightAirportPopularCityUseCase
import com.tokopedia.flight.airportv2.domain.FlightAirportSuggestionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@Module
class FlightAirportModule {

    @Provides
    @FlightAirportScope
    @Named(FlightAirportPopularCityUseCase.NAMED_FLIGHT_AIRPORT_POPULAR_CITY_QUERY)
    fun provideFlightAirportPopularCityQuery() =
            FlightAirportQuery.QUERY_AIRPORT_POPULAR_CITY

    @Provides
    @FlightAirportScope
    @Named(FlightAirportSuggestionUseCase.NAMED_FLIGHT_AIRPORT_SUGGESTION_QUERY)
    fun provideFlightAirportSuggestionQuery() =
            FlightAirportQuery.QUERY_AIRPORT_SUGGESTION

}
