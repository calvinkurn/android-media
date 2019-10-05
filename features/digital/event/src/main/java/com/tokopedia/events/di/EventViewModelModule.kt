package com.tokopedia.events.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.events.di.scope.EventScope
import com.tokopedia.events.view.customview.HolidayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@EventScope
abstract class EventViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HolidayViewModel::class)
    abstract fun bindHolidayViewModel(holidayViewModel: HolidayViewModel) : ViewModel

    @Binds
    abstract fun bindViewModelFactory( factory: ViewModelFactory):
            ViewModelProvider.Factory

}