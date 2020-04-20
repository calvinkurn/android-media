package com.tokopedia.flight.filter.di

import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.common.travel.utils.TravelProductionDispatcherProvider
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 17/02/2020
 */
@FlightFilterScope
@Module
class FlightFilterModule {

    @Provides
    @FlightFilterScope
    fun provideDispatcherProvider(): TravelDispatcherProvider = TravelProductionDispatcherProvider()

}