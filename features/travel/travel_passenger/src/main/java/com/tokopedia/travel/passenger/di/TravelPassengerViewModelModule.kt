package com.tokopedia.travel.passenger.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travel.passenger.presentation.viewmodel.TravelContactDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by furqan on 03/01/2020
 */
@Module
abstract class TravelPassengerViewModelModule {

    @TravelPassengerScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelContactDataViewModel::class)
    abstract fun travelContactDataViewModel(viewModel: TravelContactDataViewModel): ViewModel

}