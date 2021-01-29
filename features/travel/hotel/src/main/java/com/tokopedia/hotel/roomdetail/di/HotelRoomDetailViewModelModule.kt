package com.tokopedia.hotel.roomdetail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.roomdetail.presentation.viewmodel.HotelRoomDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 09/05/19
 */
@Module
abstract class HotelRoomDetailViewModelModule {

    @HotelRoomDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelRoomDetailViewModel::class)
    internal abstract fun hotelRoomDetailViewModel(viewModel: HotelRoomDetailViewModel): ViewModel
}