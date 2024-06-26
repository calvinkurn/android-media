package com.tokopedia.flight.airport.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.domain.FlightAirportMapper
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * created by @bayazidnasir on 14/11/2022
 */

@Module
class FlightAirportModule {

    @Provides
    @Named(FlightAirportMapper.NAMED_POPULAR_AIRPORT)
    fun providePopularAirportName(@ApplicationContext context: Context): String {
        return try {
            context.getString(R.string.flight_popular_airport_label)
        } catch (exception: Exception) {
            ""
        }
    }

}
