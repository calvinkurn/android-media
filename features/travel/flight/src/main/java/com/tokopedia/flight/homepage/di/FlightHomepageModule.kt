package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 27/03/2020
 */
@Module
@FlightHomepageScope
class FlightHomepageModule {

    @Provides
    fun provideFlightDashboardCache(@ApplicationContext context: Context): FlightDashboardCache =
            FlightDashboardCache(context)

}