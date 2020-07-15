package com.tokopedia.flight.search_universal.di

import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.common.travel.utils.TravelProductionDispatcherProvider
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 10/03/2020
 */
@FlightSearchUniversalScope
@Module
class FlightSearchUniversalModule {

    @Provides
    @FlightSearchUniversalScope
    fun provideDispatcherProvider(): TravelDispatcherProvider = TravelProductionDispatcherProvider()

}