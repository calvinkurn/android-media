package com.tokopedia.discovery.categoryrevamp.di

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryListViewModel
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
@DiscoveryScope
abstract class DiscoveryViewModelModule {

    @Binds
    @DiscoveryScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @DiscoveryScope
    @ViewModelKey(DiscoveryViewModel::class)
    internal abstract fun discoveryViewModel(viewModel: DiscoveryViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @DiscoveryScope
//    @ViewModelKey(DiscoveryListViewModel::class)
//    internal abstract fun discoveryListViewModel(viewModel: DiscoveryListViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @DiscoveryScope
//    @ViewModelKey(CatalogNavViewModel::class)
//    internal abstract fun catalogNavViewModel(viewModel: CatalogNavViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @DiscoveryScope
//    @ViewModelKey(CategoryNavViewModel::class)
//    internal abstract fun categoryNavViewModel(viewModel: CategoryNavViewModel): ViewModel
}