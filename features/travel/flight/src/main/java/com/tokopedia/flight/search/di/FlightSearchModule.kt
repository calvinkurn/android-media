package com.tokopedia.flight.search.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.search.presentation.util.FlightSearchCache
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 06/04/2020
 */
@Module
class FlightSearchModule {

    @Provides
    @FlightSearchScope
    fun provideFlightSearchCache(@ApplicationContext context: Context) = FlightSearchCache(context)

}