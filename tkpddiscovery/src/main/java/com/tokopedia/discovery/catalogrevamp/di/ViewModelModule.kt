package com.tokopedia.discovery.catalogrevamp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.discovery.catalogrevamp.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.discovery.catalogrevamp.viewmodel.CatalogDetailProductListingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@CatalogScope
abstract class ViewModelModule {

    @CatalogScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogDetailPageViewModel::class)
    internal abstract fun catalogDetailPageViewModel(viewModel: CatalogDetailPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogScope
    @ViewModelKey(CatalogDetailProductListingViewModel::class)
    internal abstract fun catalogDetailProductListingViewModel(viewModel: CatalogDetailProductListingViewModel): ViewModel
}
