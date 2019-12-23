package com.tokopedia.travel.destination.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.travel.homepage.presentation.viewmodel.TravelHomepageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 2019-12-23
 */

@Module
@TravelHomepageDestinationScope
abstract class TravelHomepageDestinationViewModelModule {

    @TravelHomepageDestinationScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

}