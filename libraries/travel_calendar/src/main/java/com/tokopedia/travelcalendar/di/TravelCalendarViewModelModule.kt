package com.tokopedia.travelcalendar.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travelcalendar.selectionrangecalendar.SelectionRangeCalendarViewModel
import com.tokopedia.travelcalendar.viewmodel.TravelHolidayCalendarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TravelCalendarViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelHolidayCalendarViewModel::class)
    internal abstract fun travelHolidayCalendarViewModel(customViewModel: TravelHolidayCalendarViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectionRangeCalendarViewModel::class)
    internal abstract fun selectionRangeCalendarViewModel(customViewModel: SelectionRangeCalendarViewModel): ViewModel
}