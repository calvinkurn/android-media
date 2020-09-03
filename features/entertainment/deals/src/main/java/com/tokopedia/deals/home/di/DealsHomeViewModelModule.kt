package com.tokopedia.deals.home.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.deals.common.ui.viewmodel.DealsBaseViewModel
import com.tokopedia.deals.home.ui.viewmodel.DealsHomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by jessica on 15/06/20
 */

@Module
abstract class DealsHomeViewModelModule {

    @Binds
    @DealsHomeScope
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DealsBaseViewModel::class)
    abstract fun dealsBaseViewModel(viewModel: DealsBaseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DealsHomeViewModel::class)
    abstract fun dealsHomeViewModel(viewModel: DealsHomeViewModel): ViewModel
}