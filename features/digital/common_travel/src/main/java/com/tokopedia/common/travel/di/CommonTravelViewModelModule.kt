package com.tokopedia.common.travel.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travel.passenger.presentation.viewmodel.TravelContactDataViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by resakemal on 13/05/19
 */
@Module
@CommonTravelScope
abstract class CommonTravelViewModelModule {

    @CommonTravelScope
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelContactDataViewModel::class)
    abstract fun travelContactDataViewModel(viewModel: TravelContactDataViewModel): ViewModel
}