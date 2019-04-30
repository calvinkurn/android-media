package com.tokopedia.hotel.hoteldetail.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReviewViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 26/04/19
 */

@Module
@HotelDetailScope
abstract class HotelDetailViewModelModule {

    @HotelDetailScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HotelDetailViewModel::class)
    internal abstract fun hotelDetailViewModel(viewModel: HotelDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HotelReviewViewModel::class)
    internal abstract fun hotelReviewViewModel(viewModel: HotelReviewViewModel): ViewModel

}