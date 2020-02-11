package com.tokopedia.events.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.events.di.scope.EventScope
import com.tokopedia.events.view.customview.HolidayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@EventScope
internal abstract class EventViewModelModule {

    @EventScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HolidayViewModel::class)
    internal abstract fun holidayViewModel(holidayViewModel: HolidayViewModel) : ViewModel
}