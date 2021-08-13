package com.tokopedia.hotel.cancellation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.cancellation.presentation.viewmodel.HotelCancellationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 27/04/20
 */

@Module
abstract class HotelCancellationViewModelModule {


    @Binds
    @HotelCancellationScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelCancellationViewModel::class)
    internal abstract fun hotelCancellationViewModel(viewModel: HotelCancellationViewModel): ViewModel

}