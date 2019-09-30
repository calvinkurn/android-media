package com.tokopedia.travel.homepage.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travel.homepage.presentation.viewmodel.TravelHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-08-09
 */

@Module
@TravelHomepageScope
abstract class TravelHomepageViewModelModule {

    @TravelHomepageScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TravelHomepageViewModel::class)
    internal abstract fun travelHomepageViewModel(viewModel: TravelHomepageViewModel): ViewModel

}