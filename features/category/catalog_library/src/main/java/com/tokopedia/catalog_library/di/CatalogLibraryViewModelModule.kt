package com.tokopedia.catalog_library.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.catalog_library.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CatalogLibraryViewModelModule {

    @CatalogLibraryScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogHomepageViewModel::class)
    internal abstract fun catalogHomepageViewModel(viewModel: CatalogHomepageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLihatSemuaPageViewModel::class)
    internal abstract fun catalogLihatSemuaPageViewModel(viewModel: CatalogLihatSemuaPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogLandingPageViewModel::class)
    internal abstract fun catalogLandingPageViewModel(viewModel: CatalogLandingPageViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogProductsBaseViewModel::class)
    internal abstract fun productsBaseViewModel(viewModel: CatalogProductsBaseViewModel): ViewModel

    @Binds
    @IntoMap
    @CatalogLibraryScope
    @ViewModelKey(CatalogPopularBrandsViewModel::class)
    internal abstract fun popularBrandsViewModel(viewModel: CatalogPopularBrandsViewModel): ViewModel
}
