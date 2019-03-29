package com.tokopedia.travelcalendar.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import dagger.Component

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@TravelCalendarScope
@Component(modules = arrayOf(TravelCalendarModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface TravelCalendarComponent {

    fun inject(travelCalendarFragment: TravelCalendarBottomSheet)

}
