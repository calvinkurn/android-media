package com.tokopedia.travelhomepage.homepage.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travelhomepage.homepage.presentation.viewmodel.TravelHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-08-09
 */

@Module
abstract class TravelHomepageViewModelModule {

    @TravelHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelHomepageViewModel::class)
    internal abstract fun travelHomepageViewModel(viewModel: TravelHomepageViewModel): ViewModel

}