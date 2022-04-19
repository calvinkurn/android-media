package com.tokopedia.travelhomepage.destination.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-12-23
 */

@Module
abstract class TravelDestinationViewModelModule {

    @TravelDestinationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelDestinationViewModel::class)
    internal abstract fun travelHomepageViewModel(viewModel: TravelDestinationViewModel): ViewModel

}