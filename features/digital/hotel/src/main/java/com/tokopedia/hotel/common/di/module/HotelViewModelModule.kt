package com.tokopedia.hotel.common.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.common.di.scope.HotelScope
import com.tokopedia.hotel.common.presentation.widget.hotelcalendar.HotelCalendarDialogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 04/04/19
 */
@Module
@HotelScope
abstract class HotelViewModelModule {

    @HotelScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelCalendarDialogViewModel::class)
    internal abstract fun hotelCalendarDialogViewModel(viewModel: HotelCalendarDialogViewModel): ViewModel
}