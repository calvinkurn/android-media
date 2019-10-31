package com.tokopedia.travelcalendar.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarWidget
import com.tokopedia.travelcalendar.singlecalendar.SinglePickCalendarWidget
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet
import dagger.Component

/**
 * Created by nabillasabbaha on 14/05/18.
 */
@TravelCalendarScope
@Component(modules = arrayOf(TravelCalendarModule::class, TravelCalendarViewModelModule::class),
        dependencies = arrayOf(BaseAppComponent::class))
interface TravelCalendarComponent {

    fun inject(travelCalendarFragment: TravelCalendarBottomSheet)

    fun inject(travelCalendarFragment: SinglePickCalendarWidget)

    fun inject(selectionRangeCalendarWidget: SelectionRangeCalendarWidget)

}
