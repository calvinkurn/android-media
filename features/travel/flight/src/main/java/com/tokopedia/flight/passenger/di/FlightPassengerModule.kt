package com.tokopedia.flight.passenger.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 12/03/18.
 */

@Module
class FlightPassengerModule {

    @FlightPassengerScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}
