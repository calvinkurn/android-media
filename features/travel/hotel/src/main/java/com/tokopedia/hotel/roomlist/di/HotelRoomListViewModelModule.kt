package com.tokopedia.hotel.roomlist.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 04/04/19
 */
@Module
abstract class HotelRoomListViewModelModule {

    @HotelRoomListScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelRoomListViewModel::class)
    internal abstract fun hotelDestinationViewModel(viewModel: HotelRoomListViewModel): ViewModel
}