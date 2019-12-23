package com.tokopedia.travelhomepage.destination.di

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

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