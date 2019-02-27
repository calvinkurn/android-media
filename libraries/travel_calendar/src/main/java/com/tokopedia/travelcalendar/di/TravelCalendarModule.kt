package com.tokopedia.travelcalendar.di

import com.tokopedia.travelcalendar.domain.TravelCalendarProvider
import com.tokopedia.travelcalendar.domain.TravelCalendarScheduler
import dagger.Module
import dagger.Provides

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@Module
class TravelCalendarModule {

    @Provides
    fun provideTravelCalendarProvider(): TravelCalendarProvider {
        return TravelCalendarScheduler()
    }
}
