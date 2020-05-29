package com.tokopedia.flight.bookingV3.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by alvarisi on 11/8/17.
 */
@Module
class FlightBookingModule {

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}